package com.freight.contract.graphql;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ContractQueryInput {
    private String searchTerm;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

    // 构造函数
    public ContractQueryInput() {
    }

    // 判断是否有筛选条件
    public boolean hasFilters() {
        return searchTerm != null || status != null || startDate != null || endDate != null;
    }
}