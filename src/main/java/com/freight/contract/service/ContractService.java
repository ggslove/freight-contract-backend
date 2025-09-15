package com.freight.contract.service;

import com.freight.contract.entity.Contract;
import com.freight.contract.eunus.ContractStatus;
import com.freight.contract.graphql.dto.ContractConnection;
import com.freight.contract.graphql.dto.ContractEdge;
import com.freight.contract.graphql.dto.PageInfo;
import com.freight.contract.repository.ContractRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
     * 构建 GraphQL 分页结果
     */
    public ContractConnection getContractConnection(int first, String after) {
        // 1. 解析游标（假设游标为 Base64 编码的 ID，首次查询 after 为 null）
        Long cursorId = after != null ? decodeCursor(after) : 0L;

        // 2. 查询数据（按 ID 排序，游标之后的数据，取 first+1 条用于判断是否有下一页）
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Contract> contracts = contractRepository.findByIdGreaterThan(cursorId,
                PageRequest.of(0, first + 1, sort));

        // 3. 构建边缘列表（包含节点和游标）
        List<ContractEdge> edges = contracts.stream()
                .limit(first)  // 截取 first 条作为当前页数据
                .map(contract -> new ContractEdge(
                        contract,
                        encodeCursor(contract.getId())  // 编码 ID 为游标
                ))
                .collect(Collectors.toList());

        // 4. 构建页信息（判断是否有下一页）
        boolean hasNextPage = contracts.size() > first;
        PageInfo pageInfo = new PageInfo(
                hasNextPage,
                cursorId > 0,  // 是否有上一页（游标 ID > 0 时）
                edges.isEmpty() ? null : edges.get(0).getCursor(),
                edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor()
        );

        // 5. 构建连接结果（包含总记录数）
        long totalCount = contractRepository.count();
        return new ContractConnection(edges, pageInfo, totalCount);
    }

    // 游标编码（ID -> Base64 字符串）
    private String encodeCursor(Long id) {
        return Base64.getEncoder().encodeToString(id.toString().getBytes());
    }

    // 游标解码（Base64 字符串 -> ID）
    private Long decodeCursor(String cursor) {
        byte[] decoded = Base64.getDecoder().decode(cursor);
        return Long.parseLong(new String(decoded));
    }
}