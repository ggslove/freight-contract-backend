package com.freight.contract.config.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CursorPagingRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 使用游标方式分页查询
     * @param spec 查询条件
     * @param cursorFieldName 游标字段名
     * @param cursorValue 游标值
     * @param pageable 分页参数
     * @return 分页结果
     */
    Slice<T> findAllWithCursor(Specification<T> spec, String cursorFieldName, Object cursorValue, Pageable pageable);

    /**
     * 计算符合条件且游标值大于指定值的记录数
     * @param spec 查询条件
     * @param cursorFieldName 游标字段名
     * @param cursorValue 游标值
     * @return 记录数
     */
    long countWithCursor(Specification<T> spec, String cursorFieldName, Object cursorValue);
}