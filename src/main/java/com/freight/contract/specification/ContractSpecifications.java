package com.freight.contract.specification;

import com.freight.contract.entity.Contract;
import com.freight.contract.eunus.ContractStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ContractSpecifications {

    /**
     * 根据过滤条件构建Specification
     */
    public static Specification<Contract> withFilter(String searchTerm, String status, LocalDate startDate, LocalDate endDate) {
        return (Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 搜索词过滤
            if (searchTerm != null && !searchTerm.isEmpty()) {
                predicates.add(cb.or(
                    cb.like(root.get("businessNo"), "%" + searchTerm + "%"),
                    cb.like(root.get("billNo"), "%" + searchTerm + "%"),
                    cb.like(root.get("theClient"), "%" + searchTerm + "%"),
                    cb.like(root.get("salesman"), "%" + searchTerm + "%")
                ));
            }

            // 状态过滤
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), ContractStatus.valueOf(status)));
            }

            // 日期范围过滤
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfReceipt"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateOfReceipt"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}