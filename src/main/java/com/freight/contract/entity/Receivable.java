package com.freight.contract.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "receivable")
@Data
public class Receivable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id") // 确保数据库字段映射正确
    private Contract contract;
    
    // Getter和Setter
    public Contract getContract() { return contract; }
    public void setContract(Contract contract) { this.contract = contract; }
    
    @Column(name = "customer_name", nullable = false)
    private String customerName;
    
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReceivableStatus status;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}