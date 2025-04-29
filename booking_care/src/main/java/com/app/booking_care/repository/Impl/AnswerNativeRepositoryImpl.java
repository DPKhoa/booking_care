package com.app.booking_care.repository.Impl;

import com.app.booking_care.component.NativeQueryComponent;
import com.app.booking_care.entity.AnswerEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.AnswerNativeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class AnswerNativeRepositoryImpl implements AnswerNativeRepository {
    private final NativeQueryComponent queryComponent;

    public AnswerNativeRepositoryImpl(NativeQueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    @Override
    public Page<AnswerEntity> findAllWithPaging(PagingConditionModel pagingConditionModel) {
        String sql = """
                SELECT * FROM ANSWER
               """;
        String countSql = "SELECT COUNT(A.ID) FROM ANSWER A";

        Object[] params = new Object[]{};
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return queryComponent.findPage(sql,countSql,pageable, AnswerEntity.class,params);
    }
}
