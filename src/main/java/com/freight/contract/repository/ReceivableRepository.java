package com.freight.contract.repository;

import com.freight.contract.entity.Receivable;
import com.freight.contract.entity.ReceivableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Long> {
    
    List<Receivable> findByContractId(Long contractId);
    
    List<Receivable> findByStatus(ReceivableStatus status);
    
    List<Receivable> findByCustomerNameContainingIgnoreCase(String customerName);
    
    @Query("SELECT r FROM Receivable r WHERE r.customerName LIKE %:keyword% OR r.currency LIKE %:keyword%")
    List<Receivable> searchByKeyword(@Param("keyword") String keyword);
    
    List<Receivable> findByCurrency(String currency);
    
    @Query("SELECT SUM(r.amount) FROM Receivable r WHERE r.contract.id = :contractId")
    BigDecimal getTotalReceivableByContractId(@Param("contractId") Long contractId);
    
    @Query("SELECT r FROM Receivable r WHERE r.amount > :amount")
    List<Receivable> findByAmountGreaterThan(@Param("amount") BigDecimal amount);
}