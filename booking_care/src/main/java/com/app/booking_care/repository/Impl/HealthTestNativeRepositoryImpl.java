package com.app.booking_care.repository.Impl;

import com.app.booking_care.component.NativeQueryComponent;
import com.app.booking_care.entity.HealthTestEntity;
import com.app.booking_care.entity.QuestionEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.HealthTestNativeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class HealthTestNativeRepositoryImpl implements HealthTestNativeRepository {
    private final NativeQueryComponent queryComponent;

    public HealthTestNativeRepositoryImpl(NativeQueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    @Override
    public Page<HealthTestEntity> findAllWithPaging(PagingConditionModel pagingConditionModel) {
        String sql = """
                SELECT * FROM healthtest
               """;
        String countSql = "SELECT COUNT(ht.ID) FROM healthtest ht";

        Object[] params = new Object[]{};
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return queryComponent.findPage(sql,countSql,pageable, HealthTestEntity.class,params);
    }
}
