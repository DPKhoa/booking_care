package com.app.booking_care.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NativeQueryComponent {
    private final JdbcTemplate jdbcTemplate;

    public NativeQueryComponent(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> Page<T> findPage(String sql, String countSql, Pageable pageable, Class<T> type, Object... params) {
        String paginateSql = sql + "LIMIT ? OFFSET ? ";
        Object[] paginatedParams = appendPaginationParams(params, pageable.getPageSize(), pageable.getOffset());
        List<T> content = jdbcTemplate.query(paginateSql, new BeanPropertyRowMapper<>(type), paginatedParams);
        Long total = jdbcTemplate.queryForObject(countSql, Long.class, params);
        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    public <T> List<T> findList(String sql, Class<T> type, Object... params) {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), params);
    }
    public <T> T findOne(String sql, Class<T>type, Object...params){
        List<T> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), params);
        return results.isEmpty() ? null : results.get(0);
    }
    private Object[] appendPaginationParams(Object[] originalParams, int pageSize, long offset) {
        Object[] newParams = new Object[originalParams.length + 2];
        System.arraycopy(originalParams,0, newParams, 0, originalParams.length);
        newParams[originalParams.length] = pageSize;
        newParams[originalParams.length +1 ] = offset;
        return newParams;
    }
}
