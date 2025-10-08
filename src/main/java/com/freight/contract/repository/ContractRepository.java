package com.freight.contract.repository;

import com.freight.contract.config.repository.CursorPagingRepository;
import com.freight.contract.entity.Contract;
import com.freight.contract.eunus.ContractStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends CursorPagingRepository<Contract, Long> {

    List<Contract> findByStatus(ContractStatus status);

    @Query("SELECT c FROM Contract c WHERE c.businessNo LIKE %:keyword% OR c.theClient LIKE %:keyword% OR c.salesman LIKE %:keyword%")
    List<Contract> searchByKeyword(@Param("keyword") String keyword);

    List<Contract> findByIdGreaterThan(Long id, Pageable pageable);
}