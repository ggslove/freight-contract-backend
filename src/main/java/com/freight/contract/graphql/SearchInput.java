package com.freight.contract.graphql;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class SearchInput {
    private String entityName;
    private String relSortName;
    private List<SearchParam> params;
}
