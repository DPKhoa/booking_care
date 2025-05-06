package com.app.booking_care.repository.Impl;


import com.app.booking_care.component.NativeQueryComponent;
import com.app.booking_care.entity.TestQuestionMappingEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.TestQuestionMappingNativeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class TestQuestionMappingNativeImpl implements TestQuestionMappingNativeRepository {
    private final NativeQueryComponent queryComponent;

    public TestQuestionMappingNativeImpl(NativeQueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    @Override
    public Page<TestQuestionMappingEntity> findAllWithPaging(PagingConditionModel pagingConditionModel) {
        String sql = """
                SELECT * FROM test_question_mapping
               """;
        String countSql = "SELECT COUNT(TQM.ID) FROM test_question_mapping TQM";

        Object[] params = new Object[]{};
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return queryComponent.findPage(sql,countSql,pageable, TestQuestionMappingEntity.class,params);
    }
}
