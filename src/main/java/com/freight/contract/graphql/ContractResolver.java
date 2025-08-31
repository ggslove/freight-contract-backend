package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.ContractStatus;
import com.freight.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.util.List;

@Controller
public class ContractResolver {
    
    @Autowired
    private ContractService contractService;
    
    @QueryMapping
    public List<Contract> contracts() {
        return contractService.getAllContracts();
    }
    
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
    
    @MutationMapping
    public Contract createContract(@Argument ContractInput input) {
        Contract contract = new Contract();
        contract.setBusinessNo(input.getBusinessNo());
        contract.setCustomerName(input.getCustomerName());
        contract.setSalesman(input.getSalesman());
        contract.setAmount(input.getAmount());
        contract.setCurrency(input.getCurrency());
        contract.setStatus(ContractStatus.valueOf(input.getStatus()));
        contract.setContractDate(input.getContractDate());
        contract.setDeliveryDate(input.getDeliveryDate());
        return contractService.createContract(contract);
    }
    
    @MutationMapping
    public Contract updateContract(@Argument Long id, @Argument ContractInput input) {
        Contract contractDetails = new Contract();
        contractDetails.setBusinessNo(input.getBusinessNo());
        contractDetails.setCustomerName(input.getCustomerName());
        contractDetails.setSalesman(input.getSalesman());
        contractDetails.setAmount(input.getAmount());
        contractDetails.setCurrency(input.getCurrency());
        contractDetails.setStatus(ContractStatus.valueOf(input.getStatus()));
        contractDetails.setContractDate(input.getContractDate());
        contractDetails.setDeliveryDate(input.getDeliveryDate());
        return contractService.updateContract(id, contractDetails).orElse(null);
    }
    
    @MutationMapping
    public Boolean deleteContract(@Argument Long id) {
        return contractService.deleteContract(id);
    }
}