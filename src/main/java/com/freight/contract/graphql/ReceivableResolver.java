package com.freight.contract.graphql;

import com.freight.contract.entity.Contract;
import com.freight.contract.entity.Currency;
import com.freight.contract.entity.Receivable;
import com.freight.contract.eunus.ContractStatus;
import com.freight.contract.mapper.ReceivableMapper;
import com.freight.contract.service.ContractService;
import com.freight.contract.service.CurrencyService;
import com.freight.contract.service.ReceivableService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class ReceivableResolver {
    private final CurrencyService currencyService;
    private final ReceivableService receivableService;
    private final ContractService contractService;
    private final ReceivableMapper receivableMapper;

    @QueryMapping
    public List<Receivable> receivables() {
        return receivableService.getAllReceivables();
    }

    @QueryMapping
    public Receivable receivable(@Argument Long id) {
        return receivableService.getReceivableById(id).orElse(null);
    }

    @QueryMapping
    public List<Receivable> receivablesByContract(@Argument Long contractId) {
        return receivableService.getReceivablesByContractId(contractId);
    }

    @QueryMapping
    public List<Receivable> receivablesByStatus(@Argument String status) {
        return receivableService.getReceivablesByStatus(ContractStatus.valueOf(status));
    }

    @QueryMapping
    public List<Receivable> searchReceivables(@Argument String keyword) {
        return receivableService.searchReceivables(keyword);
    }

    @MutationMapping
    public Receivable createReceivable(@Argument Long contractId, @Argument ReceivableInput receivableInput) {
        Contract contract = contractService.getContractById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + contractId));

        Receivable receivable = receivableMapper.toEntity(receivableInput, contract);
        return receivableService.createReceivable(receivable);
    }

    @MutationMapping
    public Receivable updateReceivable(@Argument Long id, @Argument ReceivableInput receivableInput) {
        Receivable existingReceivable = receivableService.getReceivableById(id)
                .orElseThrow(() -> new RuntimeException("Receivable not found with id: " + id));
        // 手动更新字段
        existingReceivable.setFinanceItem(receivableInput.getFinanceItem());
        existingReceivable.setAmount(receivableInput.getAmount());
        existingReceivable.setStatus(receivableInput.getStatus());

        // 更新币种
        if (receivableInput.getCurrencyCode() != null) {
            Currency currency = currencyService.getCurrencyByCode(receivableInput.getCurrencyCode()).orElseThrow(() ->
                    new RuntimeException("Currency not found with code: " + receivableInput.getCurrencyCode()));
            existingReceivable.setCurrency(currency);
        }

        return receivableService.updateReceivable(id, existingReceivable).orElse(null);
    }

    @MutationMapping
    public Boolean deleteReceivable(@Argument Long id) {
        return receivableService.deleteReceivable(id);
    }

    @SchemaMapping(typeName = "Contract", field = "receivables")
    public List<Receivable> getReceivablesByContract(Contract contract) {
        return receivableService.getReceivablesByContractId(contract.getId());
    }
}