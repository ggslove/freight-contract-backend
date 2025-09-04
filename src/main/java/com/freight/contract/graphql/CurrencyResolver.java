package com.freight.contract.graphql;

import com.freight.contract.entity.Currency;
import com.freight.contract.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CurrencyResolver{
    
    private final CurrencyService currencyService;
    
    @QueryMapping
    public List<Currency> currencies() {
        return currencyService.getAllCurrencies();
    }
    
    @QueryMapping
    public List<Currency> activeCurrencies() {
        return currencyService.getActiveCurrencies();
    }
    
    @QueryMapping
    public Currency currencyById(@Argument Long id) {
        return currencyService.getCurrencyById(id)
                .orElse(null);
    }
    
    @QueryMapping
    public Currency currencyByCode(@Argument String code) {
        return currencyService.getCurrencyByCode(code)
                .orElse(null);
    }
    
    @MutationMapping
    public Currency createCurrency(
            @Argument String code, 
            @Argument String name, 
            @Argument String symbol, 
            @Argument java.math.BigDecimal exchangeRate) {
        Currency currency = new Currency();
        currency.setCode(code.toUpperCase());
        currency.setName(name);
        currency.setSymbol(symbol);
        currency.setExchangeRate(exchangeRate);
        currency.setIsActive(true);
        
        return currencyService.createCurrency(currency);
    }
    
    @MutationMapping
    public Currency updateCurrency(
            @Argument Long id,
            @Argument String name, 
            @Argument String symbol, 
            @Argument java.math.BigDecimal exchangeRate, 
            @Argument Boolean isActive) {
        Currency currency = new Currency();
        currency.setName(name);
        currency.setSymbol(symbol);
        currency.setExchangeRate(exchangeRate);
        currency.setIsActive(isActive);
        
        return currencyService.updateCurrency(id, currency);
    }
    
    @MutationMapping
    public Boolean deleteCurrency(@Argument Long id) {
        return currencyService.deleteCurrency(id);
    }
}