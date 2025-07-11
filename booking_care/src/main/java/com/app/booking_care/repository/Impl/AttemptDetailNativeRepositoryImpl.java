package com.app.booking_care.repository.Impl;

import com.app.booking_care.component.NativeQueryComponent;
import com.app.booking_care.entity.AttemptDetailEntity;

import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.AttemptDetailNativeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class AttemptDetailNativeRepositoryImpl implements AttemptDetailNativeRepository {
    private final NativeQueryComponent queryComponent;

    public AttemptDetailNativeRepositoryImpl(NativeQueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    @Override
    public Page<AttemptDetailEntity> findAllWithPaging(PagingConditionModel pagingConditionModel) {
        String sql = """
                SELECT * FROM ATTEMPT_DETAIL
               """;
        String countSql = "SELECT COUNT(AD.ID) FROM ATTEMPT_DETAIL AD";

        Object[] params = new Object[]{};
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return queryComponent.findPage(sql,countSql,pageable, AttemptDetailEntity.class,params);
    }


}
