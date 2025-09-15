package com.freight.contract.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageInfo {
    private boolean hasNextPage;     // 是否有下一页
    private boolean hasPreviousPage; // 是否有上一页
    private String startCursor;      // 首条记录游标
    private String endCursor;        // 末条记录游标
}
