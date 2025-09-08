package com.freight.contract.repository;

import com.freight.contract.entity.Payable;
import com.freight.contract.eunus.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PayableRepository extends JpaRepository<Payable, Long> {

    List<Payable> findByContractId(Long contractId);

    List<Payable> findByStatus(ContractStatus status);


    @Query("SELECT p FROM Payable p WHERE p.financeItem LIKE %:keyword%")
    List<Payable> searchByKeyword(@Param("keyword") String keyword);


//    @Query("SELECT SUM(p.amount) FROM Payable p WHERE p.contract.id = :contractId")
//    BigDecimal getTotalPayableByContractId(@Param("contractId") Long contractId);


}