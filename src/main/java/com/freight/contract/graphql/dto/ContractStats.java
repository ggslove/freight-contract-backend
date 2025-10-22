package com.freight.contract.graphql.dto;

import com.freight.contract.eunus.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContractStats {
    private long count;
    private ContractStatus status;


}
