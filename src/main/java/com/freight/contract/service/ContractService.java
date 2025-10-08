package com.freight.contract.service;

import com.freight.contract.entity.Contract;
import com.freight.contract.eunus.ContractStatus;
import com.freight.contract.graphql.ContractQueryInput;
import com.freight.contract.graphql.dto.ContractConnection;
import com.freight.contract.graphql.dto.ContractEdge;
import com.freight.contract.graphql.dto.CursorInfo;
import com.freight.contract.graphql.dto.PageInfo;
import com.freight.contract.repository.ContractRepository;
import com.freight.contract.specification.ContractSpecifications;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }


    public List<Contract> getContractsByStatus(ContractStatus status) {
        return contractRepository.findByStatus(status);
    }

    public List<Contract> searchContracts(String keyword) {
        return contractRepository.searchByKeyword(keyword);
    }


    public Contract createContract(Contract contract) {
        return contractRepository.save(contract);
    }

    public Optional<Contract> updateContract(Long id, Contract contract) {
        return contractRepository.findById(id)
                .map(existing -> {
                    // 拷贝对象属性,除了id
                    BeanUtils.copyProperties(contract, existing);
                    existing.setId(id);
                    return contractRepository.save(existing);
                });
    }

    public boolean deleteContract(Long id) {
        return contractRepository.findById(id)
                .map(contract -> {
                    contractRepository.delete(contract);
                    return true;
                })
                .orElse(false);
    }


    /**
     * 构建 GraphQL 分页结果（使用Specification和游标分页）
     */
    public ContractConnection getContractConnection(int first, String after, ContractQueryInput filter, String sortField) {
        // 1. 解析游标（支持多字段排序的游标）
        CursorInfo cursorInfo = after != null ? decodeCursor(after) : new CursorInfo(0L, LocalDateTime.now(), null);

        // 2. 确定排序字段和方向
        Sort sort = determineSort(sortField);
        Pageable pageable = PageRequest.of(0, first + 1, sort);

        // 3. 构建Specification
        Specification<Contract> spec = null;
        if (filter != null && filter.hasFilters()) {
            spec = ContractSpecifications.withFilter(
                    filter.getSearchTerm(),
                    filter.getStatus(),
                    filter.getStartDate(),
                    filter.getEndDate()
            );
        }

        // 4. 确定游标字段和值
        String cursorField = determineCursorField(sortField);
        Object cursorValue = determineCursorValue(cursorInfo, cursorField);

        // 5. 使用游标分页查询
        Slice<Contract> contractSlice = contractRepository.findAllWithCursor(spec, cursorField, cursorValue, pageable);

        // 6. 计算剩余记录数
        long remainingCount = contractRepository.countWithCursor(spec, cursorField, cursorValue);

        // 7. 构建边缘列表
        List<ContractEdge> edges = contractSlice.getContent().stream()
                .limit(first)
                .map(contract -> new ContractEdge(
                        contract,
                        encodeCursor(createCursorInfo(contract, cursorField))  // 使用多字段编码游标
                ))
                .collect(Collectors.toList());

        // 8. 构建页信息
        PageInfo pageInfo = new PageInfo(
                contractSlice.hasNext(),
                hasPreviousPage(cursorInfo, cursorField),
                edges.isEmpty() ? null : edges.get(0).getCursor(),
                edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor()
        );

        // 9. 返回连接结果
        return new ContractConnection(edges, pageInfo, remainingCount);
    }


    /**
     * 游标解码（Base64 字符串 -> CursorInfo）
     */
    private CursorInfo decodeCursor(String cursor) {
        try {
            byte[] decoded = Base64.getDecoder().decode(cursor);
            String[] parts = new String(decoded).split("\\|");

            Long id = Long.parseLong(parts[0]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[1]);
            LocalDate dateOfReceipt = parts.length > 2 && !parts[2].isEmpty() ? LocalDate.parse(parts[2]) : null;

            return new CursorInfo(id, createdAt, dateOfReceipt);
        } catch (Exception e) {
            // 解码失败，返回默认游标
            return new CursorInfo(0L, LocalDateTime.now(), null);
        }
    }

    /**
     * 根据合同和游标字段创建游标信息
     */
    private CursorInfo createCursorInfo(Contract contract, String cursorField) {
        switch (cursorField) {
            case "createdAt":
                return new CursorInfo(contract.getId(), contract.getCreatedAt(), contract.getDateOfReceipt());
            case "dateOfReceipt":
                return new CursorInfo(contract.getId(), contract.getCreatedAt(), contract.getDateOfReceipt());
            case "id":
            default:
                return new CursorInfo(contract.getId(), contract.getCreatedAt(), contract.getDateOfReceipt());
        }
    }

    /**
     * 确定排序方式
     */
    private Sort determineSort(String sortField) {
        if (sortField == null || sortField.isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }

        String[] parts = sortField.split(":");
        String field = parts[0];
        Sort.Direction direction = parts.length > 1 && "DESC".equalsIgnoreCase(parts[1])
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, field);
    }

    /**
     * 确定游标字段
     */
    private String determineCursorField(String sortField) {
        if (sortField == null || sortField.isEmpty()) {
            return "id";
        }

        String[] parts = sortField.split(":");
        return parts[0];
    }

    /**
     * 确定游标值
     */
    private Object determineCursorValue(CursorInfo cursorInfo, String cursorField) {
        switch (cursorField) {
            case "id":
                return cursorInfo.getId();
            case "createdAt":
                return cursorInfo.getCreatedAt();
            case "dateOfReceipt":
                return cursorInfo.getDateOfReceipt();
            default:
                return cursorInfo.getId();
        }
    }

    /**
     * 判断是否有上一页
     */
    private boolean hasPreviousPage(CursorInfo cursorInfo, String cursorField) {
        switch (cursorField) {
            case "id":
                return cursorInfo.getId() > 0;
            case "createdAt":
                return !cursorInfo.getCreatedAt().equals(LocalDateTime.now());
            case "dateOfReceipt":
                return cursorInfo.getDateOfReceipt() != null;
            default:
                return cursorInfo.getId() > 0;
        }
    }

    /**
     * 游标编码（CursorInfo -> Base64 字符串）
     */
    private String encodeCursor(CursorInfo cursorInfo) {
        String cursorValue = cursorInfo.getId() + "|" +
                cursorInfo.getCreatedAt().toString() + "|" +
                (cursorInfo.getDateOfReceipt() != null ? cursorInfo.getDateOfReceipt().toString() : "");
        return Base64.getEncoder().encodeToString(cursorValue.getBytes());
    }


}