package com.freight.contract.graphql;

import com.freight.contract.entity.ReceivableStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceivableInput {
    private String customerName;
    private BigDecimal amount;
    private String currencyCode;
    private LocalDateTime dueDate;
    private ReceivableStatus status;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public ReceivableStatus getStatus() {
        return status;
    }

    public void setStatus(ReceivableStatus status) {
        this.status = status;
    }
}