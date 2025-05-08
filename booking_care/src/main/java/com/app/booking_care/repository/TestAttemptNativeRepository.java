package com.app.booking_care.repository;


import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;

public interface TestAttemptNativeRepository {
    Page<TestAttemptEntity> findAllWithPaging(PagingConditionModel pagingConditionModel);

}
