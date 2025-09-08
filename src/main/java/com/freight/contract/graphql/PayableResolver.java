package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.Currency;
import com.freight.contract.entity.Payable;
import com.freight.contract.mapper.PayableMapper;
import com.freight.contract.service.ContractService;
import com.freight.contract.service.CurrencyService;
import com.freight.contract.service.PayableService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class PayableResolver {

    private final PayableService payableService;
    private final CurrencyService currencyService;
    private final ContractService contractService;
    private final PayableMapper payableMapper;

    @QueryMapping
    public List<Payable> payables() {
        return payableService.getAllPayables();
    }

    @QueryMapping
    public Payable payable(@Argument Long id) {
        return payableService.getPayableById(id).orElse(null);
    }

    @QueryMapping
    public List<Payable> payablesByContract(@Argument Long contractId) {
        return payableService.getPayablesByContractId(contractId);
    }

    @MutationMapping
    public Payable createPayable(@Argument Long contractId, @Argument PayableInput payableInput) {
        Contract contract = contractService.getContractById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + contractId));

        Payable payable = payableMapper.toEntity(payableInput, contract);
        return payableService.createPayable(payable);
    }

    @MutationMapping
    public Payable updatePayable(@Argument Long id, @Argument PayableInput payableInput) {
        Payable existingPayable = payableService.getPayableById(id)
                .orElseThrow(() -> new RuntimeException("Payable not found with id: " + id));
        // 手动更新字段
        existingPayable.setFinanceItem(payableInput.getFinanceItem());
        existingPayable.setAmount(payableInput.getAmount());
        existingPayable.setStatus(payableInput.getStatus());
        // 更新币种
        if (payableInput.getCurrencyCode() != null) {
            Currency currency = currencyService.getCurrencyByCode(payableInput.getCurrencyCode()).orElseThrow(
                    () -> new RuntimeException("Currency not found with code: " + payableInput.getCurrencyCode()));
            existingPayable.setCurrency(currency);
        }
        return payableService.updatePayable(id, existingPayable).orElse(null);
    }

    @MutationMapping
    public Boolean deletePayable(@Argument Long id) {
        return payableService.deletePayable(id);
    }

    @SchemaMapping(typeName = "Contract", field = "payables")
    public List<Payable> getPayablesByContract(Contract contract) {
        return payableService.getPayablesByContractId(contract.getId());
    }
}