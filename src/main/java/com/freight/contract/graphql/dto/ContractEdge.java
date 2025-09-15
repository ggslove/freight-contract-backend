package com.freight.contract.graphql.dto;

import com.freight.contract.entity.Contract;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContractEdge {
    private Contract node;  // 实际合同数据（对应 GraphQL 的 node 字段）
    private String cursor;  // 游标（通常为 Base64 编码的 ID 或时间戳）
}
