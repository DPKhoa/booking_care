package com.app.booking_care.repository;

import com.app.booking_care.entity.HealthTestEntity;
import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;

public interface HealthTestNativeRepository {
    Page<HealthTestEntity> findAllWithPaging(PagingConditionModel pagingConditionModel);
}
