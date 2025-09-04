package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.ContractStatus;
import com.freight.contract.entity.Currency;
import com.freight.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public Contract createContract(
            @Argument String businessNo,
            @Argument String customerName,
            @Argument String billNo,
            @Argument String salesman,
            @Argument BigDecimal amount,
            @Argument String status,
            @Argument LocalDateTime contractDate,
            @Argument LocalDateTime deliveryDate,
            @Argument String description) {
        
        Contract contract = new Contract();
        contract.setBusinessNo(businessNo);
        contract.setCustomerName(customerName);
        contract.setBillNo(billNo);
        contract.setSalesman(salesman);
        contract.setAmount(amount);
        
        // 设置币种

        contract.setStatus(ContractStatus.valueOf(status));
        contract.setContractDate(contractDate);
        contract.setDeliveryDate(deliveryDate);
        contract.setDescription(description);
        return contractService.createContract(contract);
    }
    
    @MutationMapping
    public Contract updateContract(
            @Argument Long id,
            @Argument String businessNo,
            @Argument String customerName,
            @Argument String billNo,
            @Argument String salesman,
            @Argument BigDecimal amount,
            @Argument String currencyCode,
            @Argument String status,
            @Argument LocalDateTime contractDate,
            @Argument LocalDateTime deliveryDate,
            @Argument String description) {
        
        Contract contractDetails = new Contract();
        contractDetails.setBusinessNo(businessNo);
        contractDetails.setCustomerName(customerName);
        contractDetails.setBillNo(billNo);
        contractDetails.setSalesman(salesman);
        contractDetails.setAmount(amount);
        
        // 设置币种
        Currency currency = new Currency();
        currency.setCode(currencyCode);

        contractDetails.setStatus(ContractStatus.valueOf(status));
        contractDetails.setContractDate(contractDate);
        contractDetails.setDeliveryDate(deliveryDate);
        contractDetails.setDescription(description);
        return contractService.updateContract(id, contractDetails).orElse(null);
    }
    
    @MutationMapping
    public Boolean deleteContract(@Argument Long id) {
        return contractService.deleteContract(id);
    }
}