package com.freight.contract.service;

import com.freight.contract.entity.Currency;
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
    
    public Currency updateCurrency(Long id, Currency currency) {
        return currencyRepository.findById(id)
                .map(existing -> {
                    existing.setName(currency.getName());
                    existing.setSymbol(currency.getSymbol());
                    existing.setExchangeRate(currency.getExchangeRate());
                    existing.setIsActive(currency.getIsActive());
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