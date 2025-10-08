package com.freight.contract.service;


import com.freight.contract.graphql.SearchInput;
import com.freight.contract.exceptions.SqlOptException;
import com.freight.contract.utils.SpringContextUtils;
import com.freight.contract.utils.TimeFormatUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class SearchInputService {

    public static final String DOT = ".";

    public enum SqlOpt {
        $like, $eq, $neq, $gt, $lt, $isNull, $isNotNull, $isTrue, $isFalse, $in;

        public SqlOpt valueByStr(String str) {
            for (SqlOpt sqlOpt : SqlOpt.values()) {
                if (str.equalsIgnoreCase(sqlOpt.toString())) {
                    return sqlOpt;
                }
            }
            return null;
        }
    }

    //RequestParam 用form表单提交
    public Page<?> pageSearch(SearchInput searchInput, Pageable pageable, Sort sort) {
        String entityName = searchInput.getEntityName();
        Specification spec = getSpecification(sort, searchInput);
        Object bean = SpringContextUtils.getBean(entityName + "Repository");
        Assert.isTrue(bean != null, String.format("spring中没有找到%sRepository接口类", entityName));
        Assert.isTrue(bean instanceof JpaSpecificationExecutor, String.format("spring中%sRepository不是JpaSpecificationExecutor类型接口", entityName));
        // ((JpaSpecificationExecutor) bean).count(spec);
        return ((JpaSpecificationExecutor) bean).findAll(spec, pageable);
    }


    public List<?> search(SearchInput searchInput, Sort sort) {
        String entityName = searchInput.getEntityName();
        Specification spec = getSpecification(sort, searchInput);
        Object bean = SpringContextUtils.getBean(entityName + "Repository");
        Assert.isTrue(bean != null, String.format("spring中没有找到%sRepository接口类", entityName));
        Assert.isTrue(bean instanceof JpaSpecificationExecutor, String.format("spring中%sRepository不是JpaSpecificationExecutor类型接口", entityName));
        return ((JpaSpecificationExecutor) bean).findAll(spec);
    }

    private Specification getSpecification(Sort sort, SearchInput searchInput) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            searchInput.getParams().stream().forEach(param -> {
                predicates.add(keyToPredicate(param.getKey(), param.getValue(), root, criteriaBuilder));
            });

            sort.forEach(order -> {
                if (order.getDirection() == Sort.Direction.ASC) {
                    query.orderBy(criteriaBuilder.asc(root.get(order.getProperty())));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get(order.getProperty())));
                }
            });
            if (searchInput.getRelSortName() != null) {
                String orderByValue = searchInput.getRelSortName();
                List<Order> orders = new ArrayList<>();
                query.groupBy(root);
                if (orderByValue.indexOf(",") != -1) {//多个排序
                    for (String orderByToken : orderByValue.split(",")) {
                        orders.add(doOrderByToken(root, criteriaBuilder, orderByToken));
                    }
                } else {
                    orders.add(doOrderByToken(root, criteriaBuilder, orderByValue));
                }
                query.orderBy(orders);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    /**
     * @param root
     * @param criteriaBuilder
     * @param orderByToken
     * @return
     */
    private Order doOrderByToken(Root root, CriteriaBuilder criteriaBuilder, String orderByToken) {
        boolean isDesc = false;
        String relations = null;
        //先临时处理join on OrderBy 条件
        Join join = root.join(relations, JoinType.LEFT);
        if (isDesc) {
            return criteriaBuilder.desc(criteriaBuilder.countDistinct(join));
        } else {
            return criteriaBuilder.asc(criteriaBuilder.countDistinct(join));
        }
    }


    //实体，关联，属性
    private Predicate keyToPredicate(String key, String value, Root root, CriteriaBuilder criteriaBuilder) {
        String relationStr = key.lastIndexOf(DOT) != -1 ? key.substring(0, key.lastIndexOf(DOT)) : key;
        List<String> relations = new ArrayList<>();
        if (relationStr.indexOf(DOT) == -1) {
            relations.add(relationStr);
        } else {
            relations = Arrays.asList(relationStr.split("\\."));
        }
        String sqlOptStr = key.substring(key.lastIndexOf(DOT) + 1, key.length());
        SqlOpt sqlOpt = SqlOpt.valueOf(sqlOptStr);
        Assert.isTrue(sqlOpt != null, String.format("%s参数最后位%s,没有对应枚举类型", key, sqlOptStr));
        //a.b.c.d
        //当有join时
        Expression expression = null;
        if (relations.size() > 1) {
            Join join = null;
            for (int i = 0; i < relations.size(); i++) {
                String rel = relations.get(i);
                if (i != relations.size() - 1) {
                    if (i == 0) {
                        join = root.join(rel, JoinType.LEFT);
                    } else {
                        join = join.join(rel, JoinType.LEFT);
                    }
                } else {
                    //最后一个
                    expression = join.get(rel);
                }
            }
        } else {
            expression = root.get(relations.get(0));
        }
        Class clzz = expression.getJavaType();
        boolean isEnum = clzz.isEnum();
        boolean isDate = clzz == Date.class;
        if (isEnum) {
            return handleEnum(sqlOpt, clzz, value, criteriaBuilder, expression);
        } else if (isDate) {
            return handleDate(sqlOpt, value, criteriaBuilder, expression);
        } else {
            return handleNormal(sqlOpt, value, criteriaBuilder, expression);
        }
    }


    private Predicate handleNormal(SqlOpt sqlOpt, String value, CriteriaBuilder criteriaBuilder, Expression expression) {
        switch (sqlOpt) {
            case $like:
                return criteriaBuilder.like(expression, "%" + value + "%");
            case $eq:
                return criteriaBuilder.equal(expression, value);
            case $neq:
                return criteriaBuilder.notEqual(expression, value);
            case $gt:
                return criteriaBuilder.greaterThan(expression, value);
            case $lt:
                return criteriaBuilder.lessThan(expression, value);
            case $isNull:
                return criteriaBuilder.isNull(expression);
            case $isNotNull:
                return criteriaBuilder.isNotNull(expression);
            case $isTrue:
                return criteriaBuilder.isTrue(expression);
            case $isFalse:
                return criteriaBuilder.isFalse(expression);
            case $in:
                CriteriaBuilder.In in = criteriaBuilder.in(expression);
                for (String i : value.replace("[", "").replace("]", "").replace("'", "").replace("\"", "").split(",")) {
                    in.value(i);
                }
                return in;
            default:
                throw new SqlOptException(String.format("Normal参数不能用%s逻辑", sqlOpt));
        }
    }


    private Object getEnumValue(Class clazz, String value) {
        for (Object obj : clazz.getEnumConstants()) {
            if (obj.toString().equalsIgnoreCase(value)) {
                return obj;
            }
        }
        throw new SqlOptException(String.format("类 %s 没有 %s的枚举对象", clazz, value));
    }

    private TimeFormatUtil timeFormatUtil = new TimeFormatUtil();

    private Date getDateValue(String value) {
        return timeFormatUtil.convert(value);
    }

    private Predicate handleEnum(SqlOpt sqlOpt, Class clzz, String value, CriteriaBuilder criteriaBuilder, Expression expression) {
        switch (sqlOpt) {
            case $like:
                throw new SqlOptException("枚举参数不能用$like逻辑");
            case $eq:
                return criteriaBuilder.equal(expression, getEnumValue(clzz, value));
            case $neq:
                return criteriaBuilder.notEqual(expression, getEnumValue(clzz, value));
            case $in:
                CriteriaBuilder.In in = criteriaBuilder.in(expression);
                for (String i : value.replace("[", "").replace("]", "").replace("'", "").replace("\"", "").split(",")) {
                    in.value(i);
                }
                return in;
            case $isNull:
                return criteriaBuilder.isNull(expression);
            case $gt:
            case $lt:
            case $isTrue:
            case $isFalse:
            default:
                throw new SqlOptException(String.format("枚举参数不能用%s逻辑", sqlOpt));
        }
    }

    private Predicate handleDate(SqlOpt sqlOpt, String value, CriteriaBuilder criteriaBuilder, Expression expression) {
        switch (sqlOpt) {
            case $eq:
                return criteriaBuilder.equal(expression, getDateValue(value));
            case $gt:
                return criteriaBuilder.greaterThan(expression, getDateValue(value));
            case $lt:
                return criteriaBuilder.lessThan(expression, getDateValue(value));
            case $neq:
                return criteriaBuilder.notEqual(expression, getDateValue(value));
            case $isNull:
                return criteriaBuilder.isNull(expression);
            case $like:
            case $in:
            case $isTrue:
            case $isFalse:
            default:
                throw new SqlOptException(String.format("日期参数不能用%s逻辑", sqlOpt));
        }
    }

    private String getClassEntityName(Class clzz) {
        String entityName = clzz.getName().substring(clzz.getName().lastIndexOf(".") + 1, clzz.getName().length());
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

}
