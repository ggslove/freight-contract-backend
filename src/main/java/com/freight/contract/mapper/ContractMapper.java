package com.freight.contract.mapper;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.Currency;
import com.freight.contract.entity.Payable;
import com.freight.contract.entity.Receivable;
import com.freight.contract.graphql.PayableInput;
import com.freight.contract.graphql.ReceivableInput;
import com.freight.contract.repository.CurrencyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring"
)
public abstract class ContractMapper {

    @Autowired
    protected CurrencyRepository currencyRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contract", source = "contract")
    @Mapping(target = "currency", expression = "java(mapCurrency(input.getCurrencyCode()))")
    @Mapping(target = "status", source = "input.status")
    @Mapping(target = "customerName", source = "input.customerName")
    @Mapping(target = "amount", source = "input.amount")
    @Mapping(target = "dueDate", source = "input.dueDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Receivable toEntity(ReceivableInput input, Contract contract);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contract", source = "contract")
    @Mapping(target = "currency", expression = "java(mapCurrency(input.getCurrencyCode()))")
    @Mapping(target = "status", source = "input.status")
    @Mapping(target = "supplierName", source = "input.supplierName")
    @Mapping(target = "amount", source = "input.amount")
    @Mapping(target = "dueDate", source = "input.dueDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Payable toEntity(PayableInput input, Contract contract);

    protected Currency mapCurrency(String currencyCode) {
        if (currencyCode == null) {
            return null;
        }
        return currencyRepository.findByCode(currencyCode).orElse(null);
    }

}