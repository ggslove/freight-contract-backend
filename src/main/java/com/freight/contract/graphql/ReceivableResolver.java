package com.freight.contract.graphql;

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
    public Receivable createReceivable(@Argument ReceivableInput input) {
        Receivable receivable = new Receivable();
        receivable.setCustomerName(input.getCustomerName());
        receivable.setAmount(input.getAmount());
        receivable.setCurrency(input.getCurrency());
        receivable.setDueDate(input.getDueDate());
        receivable.setStatus(ReceivableStatus.valueOf(input.getStatus()));
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
    public List<Receivable> getReceivablesByContract(Long contractId) {
        return receivableService.getReceivablesByContractId(contractId);
    }
}