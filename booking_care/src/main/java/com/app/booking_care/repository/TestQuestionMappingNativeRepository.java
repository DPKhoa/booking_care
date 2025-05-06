package com.app.booking_care.repository;

import com.app.booking_care.entity.TestQuestionMappingEntity;
import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;

public interface TestQuestionMappingNativeRepository {
    Page<TestQuestionMappingEntity> findAllWithPaging(PagingConditionModel pagingConditionModel);
}
