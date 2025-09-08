package com.freight.contract.mapper;

import com.freight.contract.entity.Currency;
import com.freight.contract.graphql.CurrencyInput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    Currency toEntity(CurrencyInput input);
}
