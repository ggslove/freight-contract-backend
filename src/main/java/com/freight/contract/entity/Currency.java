package com.freight.contract.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency")
@Data
public class Currency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", unique = true, nullable = false, length = 3)
    private String code;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;
    
    @Column(name = "exchange_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal exchangeRate;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
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