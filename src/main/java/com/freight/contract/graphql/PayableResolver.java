package com.freight.contract.graphql;

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
    public Payable createPayable(@Argument PayableInput input) {
        Payable payable = new Payable();
        payable.setSupplierName(input.getSupplierName());
        payable.setAmount(input.getAmount());
        payable.setCurrency(input.getCurrency());
        payable.setDueDate(input.getDueDate());
        payable.setStatus(PayableStatus.valueOf(input.getStatus()));
        return payableService.createPayable(payable);
    }
    
    @MutationMapping
    public Payable updatePayable(@Argument Long id, @Argument PayableInput input) {
        Payable payableDetails = new Payable();
        payableDetails.setSupplierName(input.getSupplierName());
        payableDetails.setAmount(input.getAmount());
        payableDetails.setCurrency(input.getCurrency());
        payableDetails.setDueDate(input.getDueDate());
        payableDetails.setStatus(PayableStatus.valueOf(input.getStatus()));
        return payableService.updatePayable(id, payableDetails).orElse(null);
    }
    
    @MutationMapping
    public Boolean deletePayable(@Argument Long id) {
        return payableService.deletePayable(id);
    }
    
    @SchemaMapping(typeName = "Contract", field = "payables")
    public List<Payable> getPayablesByContract(Long contractId) {
        return payableService.getPayablesByContractId(contractId);
    }
}