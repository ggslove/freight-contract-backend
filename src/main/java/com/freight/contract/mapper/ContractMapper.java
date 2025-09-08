package com.freight.contract.mapper;

import com.freight.contract.entity.Contract;
import com.freight.contract.graphql.ContractInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessNo", source = "businessNo")
    @Mapping(target = "billNo", source = "billNo")
    @Mapping(target = "salesman", source = "salesman")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "dateOfReceipt", source = "dateOfReceipt")
    @Mapping(target = "dateOfSailing", source = "dateOfSailing")
    @Mapping(target = "remarks", source = "remarks")
    Contract toEntity(ContractInput input);


}