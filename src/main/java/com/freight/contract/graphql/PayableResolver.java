package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.Currency;
import com.freight.contract.entity.Payable;
import com.freight.contract.entity.PayableStatus;
import com.freight.contract.service.PayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.util.List;

@Controller
public class PayableResolver {
    
    @Autowired
    private PayableService payableService;
    
    @QueryMapping
    public List<Payable> payables() {
        return payableService.getAllPayables();
    }
    
    @QueryMapping
    public Payable payable(@Argument Long id) {
        return payableService.getPayableById(id).orElse(null);
    }
    
    @QueryMapping
    public List<Payable> payablesByContract(@Argument Long contractId) {
        return payableService.getPayablesByContractId(contractId);
    }
    
    @QueryMapping
    public List<Payable> payablesByStatus(@Argument String status) {
        return payableService.getPayablesByStatus(PayableStatus.valueOf(status));
    }
    
    @QueryMapping
    public List<Payable> searchPayables(@Argument String keyword) {
        return payableService.searchPayables(keyword);
    }
    
    @MutationMapping
    public Payable createPayable(@Argument Long contractId, @Argument String supplierName,
                               @Argument java.math.BigDecimal amount, @Argument String currencyCode,
                               @Argument java.time.LocalDateTime dueDate, @Argument String status) {
        Payable payable = new Payable();
        
        // 设置关联的合同
        Contract contract = new Contract();
        contract.setId(contractId);
        payable.setContract(contract);
        
        // 设置币种
        Currency currency = new Currency();
        currency.setCode(currencyCode != null ? currencyCode : "CNY");
        payable.setCurrency(currency);
        
        // 添加空值检查和默认值
        payable.setSupplierName(supplierName != null ? supplierName : "未知供应商");
        payable.setAmount(amount != null ? amount : java.math.BigDecimal.ZERO);
        payable.setDueDate(dueDate);
        
        // 安全处理状态
        String statusStr = status;
        if (statusStr == null || statusStr.trim().isEmpty()) {
            statusStr = "PENDING";
        }
        try {
            payable.setStatus(PayableStatus.valueOf(statusStr));
        } catch (IllegalArgumentException e) {
            payable.setStatus(PayableStatus.PENDING);
        }
        
        return payableService.createPayable(payable);
    }
    
    @MutationMapping
    public Payable updatePayable(@Argument Long id, @Argument PayableInput input) {
        Payable payableDetails = new Payable();
        payableDetails.setSupplierName(input.getSupplierName());
        payableDetails.setAmount(input.getAmount());
        
        // 设置币种
        Currency currency = new Currency();
        currency.setCode(input.getCurrencyCode());
        payableDetails.setCurrency(currency);
        
        payableDetails.setDueDate(input.getDueDate());
        payableDetails.setStatus(input.getStatus());
        return payableService.updatePayable(id, payableDetails).orElse(null);
    }
    
    @MutationMapping
    public Boolean deletePayable(@Argument Long id) {
        return payableService.deletePayable(id);
    }
    
    @SchemaMapping(typeName = "Contract", field = "payables")
    public List<Payable> getPayablesByContract(Contract contract) {
        return payableService.getPayablesByContractId(contract.getId());
    }
}