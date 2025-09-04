package com.freight.contract.repository;

import com.freight.contract.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    
    Optional<Currency> findByCode(String code);
    
    List<Currency> findByIsActive(Boolean isActive);
    
    boolean existsByCode(String code);
}