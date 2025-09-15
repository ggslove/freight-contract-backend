package com.freight.contract.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ContractConnection {
    private List<ContractEdge> edges; // 边缘列表（包含合同数据和游标）
    private PageInfo pageInfo;        // 分页元信息
    private long totalCount;       // 总记录数（可选）
}
