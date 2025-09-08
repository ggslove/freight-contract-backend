package com.freight.contract.graphql;

import com.freight.contract.eunus.ContractStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ContractInput {
    //业务编号 Business No.
    private String businessNo;
    // 提单号
    private String billNo;
    // 业务员
    private String salesman;
    // 发票号  Inv No.
    private String invoiceNo;
    // 客户抬头
    private String theClient;
    // 数量
    private String quantity;
    // 状态
    private ContractStatus status;
    //收货日期
    private LocalDateTime dateOfReceipt;
    //开航日期 Date of Sailing
    private LocalDateTime dateOfSailing;


    //备注
    private String remarks;
}