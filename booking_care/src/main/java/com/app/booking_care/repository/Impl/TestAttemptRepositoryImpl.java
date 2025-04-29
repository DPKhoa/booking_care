package com.app.booking_care.repository.Impl;

import com.app.booking_care.component.NativeQueryComponent;
import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.TestAttemptNativeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class TestAttemptRepositoryImpl implements TestAttemptNativeRepository {
    private final NativeQueryComponent queryComponent;

    public TestAttemptRepositoryImpl(NativeQueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    @Override
    public Page<TestAttemptEntity> findAllWithPaging(PagingConditionModel pagingConditionModel) {
        String sql = """
                SELECT * FROM TEST ATTEMPT
               """;
        String countSql = "SELECT COUNT(TA.ID) FROM TEST ATTEMPT TA";

        Object[] params = new Object[]{};
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return queryComponent.findPage(sql,countSql,pageable, TestAttemptEntity.class,params);
    }


}
