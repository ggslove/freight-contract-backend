package com.freight.contract.entity;

import com.freight.contract.eunus.ContractStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contract")
@Data
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 或其他合适的生成策略
    private Long id;
    //业务编号 Business No.
    @Column(name = "business_no", unique = true, nullable = false)
    private String businessNo;
    // 提单号
    @Column(name = "bill_no")
    private String billNo;
    // 业务员
    @Column(name = "salesman")
    private String salesman;
    // 发票号  Inv No.
    @Column(name = "invoice_no")
    private String invoiceNo;
    // 客户抬头
    @Column(name = "the_client", nullable = false)
    private String theClient;
    // 数量
    @Column(name = "quantity", nullable = false)
    private String quantity;
    //收货日期
    @Column(name = "date_of_receipt")
    private LocalDate dateOfReceipt;
    //开航日期 Date of Sailing
    @Column(name = "date_of_sailing")
    private LocalDate dateOfSailing;
    //备注
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Receivable> receivables;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payable> payables;

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