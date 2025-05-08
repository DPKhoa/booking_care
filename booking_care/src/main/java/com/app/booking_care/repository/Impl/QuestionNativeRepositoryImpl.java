package com.app.booking_care.repository.Impl;

import com.app.booking_care.component.NativeQueryComponent;
import com.app.booking_care.entity.QuestionEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.QuestionNativeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class QuestionNativeRepositoryImpl implements QuestionNativeRepository {
    private final NativeQueryComponent queryComponent;

    public QuestionNativeRepositoryImpl(NativeQueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    @Override
    public Page<QuestionEntity> findAllWithPaging(PagingConditionModel pagingConditionModel) {
        String sql = """
                SELECT * FROM QUESTION
               """;
        String countSql = "SELECT COUNT(Q.ID) FROM QUESTION Q";

        Object[] params = new Object[]{};
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return queryComponent.findPage(sql,countSql,pageable,QuestionEntity.class,params);
    }
}
