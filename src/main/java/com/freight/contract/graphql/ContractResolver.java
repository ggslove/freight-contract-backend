package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.Payable;
import com.freight.contract.entity.Receivable;
import com.freight.contract.eunus.ContractStatus;
import com.freight.contract.graphql.dto.ContractConnection;
import com.freight.contract.graphql.dto.ContractStats;
import com.freight.contract.mapper.ContractMapper;
import com.freight.contract.mapper.PayableMapper;
import com.freight.contract.mapper.ReceivableMapper;
import com.freight.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ContractResolver {

    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private PayableMapper payableMapper;
    @Autowired
    private ReceivableMapper receivableMapper;


    @QueryMapping
    public Contract contract(@Argument Long id) {
        return contractService.getContractById(id).orElse(null);
    }

    @QueryMapping
    public List<Contract> contractsByStatus(@Argument String status) {
        return contractService.getContractsByStatus(ContractStatus.valueOf(status));
    }

    @QueryMapping
    public List<Contract> searchContracts(@Argument String keyword) {
        return contractService.searchContracts(keyword);
    }

    @QueryMapping
    public List<ContractStats> contractStats(@Argument ContractQueryInput filter) {
        return contractService.getContractStats(filter);
    }


    @MutationMapping
    public Contract createContract(@Argument ContractInput contractInput,
                                   @Argument List<ReceivableInput> receivableInputs,
                                   @Argument List<PayableInput> payableInputs
    ) {
        Contract contract = contractMapper.toEntity(contractInput);
        List<Receivable> receivables = receivableInputs.stream()
                .map(input -> receivableMapper.toEntity(input, contract))
                .collect(Collectors.toList());
        List<Payable> payables = payableInputs.stream()
                .map(input -> payableMapper.toEntity(input, contract))
                .collect(Collectors.toList());
        contract.setReceivables(receivables);
        contract.setPayables(payables);
        return contractService.createContract(contract);
    }

    @MutationMapping
    public Contract updateContract(
            @Argument Long id, @Argument ContractInput contractInput) {
        Contract contract = contractMapper.toEntity(contractInput);
        //TODO:修改处理
        return contractService.updateContract(id, contract).orElse(null);
    }

    /**
     * GraphQL 合同分页查询
     *
     * @param first  每页条数（必填）
     * @param after  游标（可选，用于加载下一页，首次查询可不传）
     * @param filter 筛选条件（可选）
     * @return 分页结果（包含数据和分页元信息）
     */
    @QueryMapping
    public ContractConnection contracts(
            @Argument int first,
            @Argument String after,
            @Argument("filter") ContractQueryInput filter,
            @Argument String sortField
    ) {
        return contractService.getContractConnection(first, after, filter, sortField);
    }

    @MutationMapping
    public Boolean deleteContract(@Argument Long id) {
        return contractService.deleteContract(id);
    }
}