package com.freight.contract.repository;

import com.freight.contract.entity.Contract;
import com.freight.contract.eunus.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    
    List<Contract> findByStatus(ContractStatus status);

    @Query("SELECT c FROM Contract c WHERE c.businessNo LIKE %:keyword% OR c.theClient LIKE %:keyword% OR c.salesman LIKE %:keyword%")
    List<Contract> searchByKeyword(@Param("keyword") String keyword);
}