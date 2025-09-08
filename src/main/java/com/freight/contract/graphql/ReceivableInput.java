package com.freight.contract.graphql;

import com.freight.contract.eunus.ContractStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReceivableInput {
    private String financeItem;
    private String amount;
    private String currencyCode;
    private ContractStatus status;
}