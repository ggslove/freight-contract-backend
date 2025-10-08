package com.freight.contract.config.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CursorPagingRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
        implements CursorPagingRepository<T, ID> {

    private final EntityManager entityManager;

    public CursorPagingRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public CursorPagingRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<T> findAllWithCursor(Specification<T> spec, String cursorFieldName, Object cursorValue, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getDomainClass());
        Root<T> root = query.from(getDomainClass());

        // 应用Specification条件
        Predicate predicate = spec != null ? spec.toPredicate(root, query, cb) : cb.conjunction();

        // 添加游标条件
        Path<Object> cursorPath = root.get(cursorFieldName);
        Predicate cursorPredicate = createCursorPredicate(cb, cursorPath, cursorValue);

        // 组合所有条件
        query.where(predicate, cursorPredicate);

        // 应用排序
        if (pageable.getSort() != null) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                if (order.getDirection() == org.springframework.data.domain.Sort.Direction.ASC) {
                    orders.add(cb.asc(root.get(order.getProperty())));
                } else {
                    orders.add(cb.desc(root.get(order.getProperty())));
                }
            });
            query.orderBy(orders);
        } else {
            // 默认按游标字段排序
            query.orderBy(cb.asc(cursorPath));
        }


        // 创建查询
        TypedQuery<T> typedQuery = entityManager.createQuery(query);

        // 应用分页
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize() + 1); // 多取一条，用于判断是否有下一页

        // 执行查询
        List<T> content = typedQuery.getResultList();

        // 判断是否有下一页
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public long countWithCursor(Specification<T> spec, String cursorFieldName, Object cursorValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> root = query.from(getDomainClass());

        // 应用Specification条件
        Predicate predicate = spec != null ? spec.toPredicate(root, query, cb) : cb.conjunction();

        // 添加游标条件
        Path<Object> cursorPath = root.get(cursorFieldName);
        Predicate cursorPredicate = createCursorPredicate(cb, cursorPath, cursorValue);

        // 组合所有条件
        query.where(predicate, cursorPredicate);

        // 设置计数查询
        query.select(cb.count(root));

        // 执行查询
        return entityManager.createQuery(query).getSingleResult();
    }

    /**
     * 根据游标值类型创建相应的比较谓词
     */
    @SuppressWarnings("unchecked")
    private <Y> Predicate createCursorPredicate(CriteriaBuilder cb, Path<Y> path, Object value) {
        if (value == null) {
            return cb.isNotNull(path);
        }

        if (value instanceof Long) {
            return cb.greaterThan((Path<Long>) path, (Long) value);
        } else if (value instanceof Integer) {
            return cb.greaterThan((Path<Integer>) path, (Integer) value);
        } else if (value instanceof String) {
            return cb.greaterThan((Path<String>) path, (String) value);
        } else if (value instanceof LocalDateTime) {
            return cb.greaterThan((Path<LocalDateTime>) path, (LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return cb.greaterThan((Path<LocalDate>) path, (LocalDate) value);
        } else if (value instanceof Double) {
            return cb.greaterThan((Path<Double>) path, (Double) value);
        } else if (value instanceof Float) {
            return cb.greaterThan((Path<Float>) path, (Float) value);
        } else {
            // 对于不支持的类型，尝试使用toString()比较
            return cb.greaterThan(path.as(String.class), value.toString());
        }
    }
}