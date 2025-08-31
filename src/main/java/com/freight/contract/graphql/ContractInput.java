package com.freight.contract.graphql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContractInput {
    private String businessNo;
    private String customerName;
    private String salesman;
    private LocalDateTime contractDate;
    private BigDecimal amount;
    private String currency;
    private String status;
    
    public String getBusinessNo() {
        return businessNo;
    }
    
    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getSalesman() {
        return salesman;
    }
    
    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
    
    public LocalDateTime getContractDate() {
        return contractDate;
    }
    
    public void setContractDate(LocalDateTime contractDate) {
        this.contractDate = contractDate;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}