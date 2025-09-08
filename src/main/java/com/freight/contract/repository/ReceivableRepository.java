package com.freight.contract.repository;

import com.freight.contract.entity.Receivable;
import com.freight.contract.eunus.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Long> {

    List<Receivable> findByContractId(Long contractId);

    List<Receivable> findByStatus(ContractStatus status);

    @Query("SELECT r FROM Receivable r WHERE r.financeItem LIKE %:keyword% ")
    List<Receivable> searchByKeyword(@Param("keyword") String keyword);

//    @Query("SELECT SUM(r.amount) FROM Receivable r WHERE r.contract.id = :contractId")
//    BigDecimal getTotalReceivableByContractId(@Param("contractId") Long contractId);

}