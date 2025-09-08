package com.freight.contract.graphql;

import com.freight.contract.entity.PayableStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PayableInput {
    private String supplierName;
    private BigDecimal amount;
    private String currencyCode;
    private LocalDateTime dueDate;
    private PayableStatus status;
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public PayableStatus getStatus() {
        return status;
    }
    
    public void setStatus(PayableStatus status) {
        this.status = status;
    }
}