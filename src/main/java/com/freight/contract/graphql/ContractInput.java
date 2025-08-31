package com.freight.contract.graphql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContractInput {
    private String businessNo;
    private String customerName;
    private String billNo;
    private String salesman;
    private LocalDateTime contractDate;
    private LocalDateTime deliveryDate;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String description;
    
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
    
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
    
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}