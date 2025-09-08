package com.freight.contract.graphql;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyInput {
    private String code;
    private String name;
    private String symbol;
    private java.math.BigDecimal exchangeRate;
    private Boolean isActive;
}
