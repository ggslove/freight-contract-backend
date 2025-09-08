package com.freight.contract.service;

import com.freight.contract.entity.Currency;
import com.freight.contract.graphql.CurrencyInput;
import com.freight.contract.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public List<Currency> getActiveCurrencies() {
        return currencyRepository.findByIsActive(true);
    }

    public Optional<Currency> getCurrencyById(Long id) {
        return currencyRepository.findById(id);
    }

    public Optional<Currency> getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    public Currency createCurrency(Currency currency) {
        if (currencyRepository.existsByCode(currency.getCode())) {
            throw new RuntimeException("Currency code already exists: " + currency.getCode());
        }
        return currencyRepository.save(currency);
    }

    public Currency updateCurrency(Long id, CurrencyInput currencyInput) {
        return currencyRepository.findById(id)
                .map(existing -> {
                    existing.setCode(currencyInput.getCode());
                    existing.setName(currencyInput.getName());
                    existing.setSymbol(currencyInput.getSymbol());
                    existing.setExchangeRate(currencyInput.getExchangeRate());
                    existing.setIsActive(currencyInput.getIsActive());
                    return currencyRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
    }

    public boolean deleteCurrency(Long id) {
        return currencyRepository.findById(id)
                .map(currency -> {
                    currencyRepository.delete(currency);
                    return true;
                })
                .orElse(false);
    }
}