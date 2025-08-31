package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.Receivable;
import com.freight.contract.entity.ReceivableStatus;
import com.freight.contract.service.ReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.util.List;

@Controller
public class ReceivableResolver {
    
    @Autowired
    private ReceivableService receivableService;
    
    @QueryMapping
    public List<Receivable> receivables() {
        return receivableService.getAllReceivables();
    }
    
    @QueryMapping
    public Receivable receivable(@Argument Long id) {
        return receivableService.getReceivableById(id).orElse(null);
    }
    
    @QueryMapping
    public List<Receivable> receivablesByContract(@Argument Long contractId) {
        return receivableService.getReceivablesByContractId(contractId);
    }
    
    @QueryMapping
    public List<Receivable> receivablesByStatus(@Argument String status) {
        return receivableService.getReceivablesByStatus(ReceivableStatus.valueOf(status));
    }
    
    @QueryMapping
    public List<Receivable> searchReceivables(@Argument String keyword) {
        return receivableService.searchReceivables(keyword);
    }
    
    @MutationMapping
    public Receivable createReceivable(@Argument Long contractId, @Argument String customerName, 
                                     @Argument java.math.BigDecimal amount, @Argument String currency,
                                     @Argument java.time.LocalDateTime dueDate, @Argument String status) {
        Receivable receivable = new Receivable();
        
        // 设置关联的合同
        Contract contract = new Contract();
        contract.setId(contractId);
        receivable.setContract(contract);
        
        // 添加空值检查和默认值
        receivable.setCustomerName(customerName != null ? customerName : "未知客户");
        receivable.setAmount(amount != null ? amount : java.math.BigDecimal.ZERO);
        receivable.setCurrency(currency != null ? currency : "CNY");
        receivable.setDueDate(dueDate);
        
        // 安全处理状态
        String statusStr = status;
        if (statusStr == null || statusStr.trim().isEmpty()) {
            statusStr = "PENDING";
        }
        try {
            receivable.setStatus(ReceivableStatus.valueOf(statusStr));
        } catch (IllegalArgumentException e) {
            receivable.setStatus(ReceivableStatus.PENDING);
        }
        
        return receivableService.createReceivable(receivable);
    }
    
    @MutationMapping
    public Receivable updateReceivable(@Argument Long id, @Argument ReceivableInput input) {
        Receivable receivableDetails = new Receivable();
        receivableDetails.setCustomerName(input.getCustomerName());
        receivableDetails.setAmount(input.getAmount());
        receivableDetails.setCurrency(input.getCurrency());
        receivableDetails.setDueDate(input.getDueDate());
        receivableDetails.setStatus(ReceivableStatus.valueOf(input.getStatus()));
        return receivableService.updateReceivable(id, receivableDetails).orElse(null);
    }
    
    @MutationMapping
    public Boolean deleteReceivable(@Argument Long id) {
        return receivableService.deleteReceivable(id);
    }
    
    @SchemaMapping(typeName = "Contract", field = "receivables")
    public List<Receivable> getReceivablesByContract(Contract contract) {
        return receivableService.getReceivablesByContractId(contract.getId());
    }
}