package com.app.booking_care.repository;

import com.app.booking_care.entity.AttemptDetailEntity;

import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;

public interface AttemptDetailNativeRepository {
    Page<AttemptDetailEntity> findAllWithPaging(PagingConditionModel pagingConditionModel);
    Long calculateTotalScore(Long  attemptId);
}
