package com.freight.contract.controller.req;

import lombok.Data;

import java.util.HashMap;

@Data
public class SearchReq extends HashMap<String, String> {
    public static String ENTITY_NAME = "entityName";
    public static String REL_SORT_NAME = "relSortName";




    public String getEntityName() {
        return get(ENTITY_NAME);
    }

    public String getRelSortName() {
        return get(REL_SORT_NAME);
    }
}
