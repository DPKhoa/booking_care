package com.app.booking_care.repository;

import com.app.booking_care.entity.AnswerEntity;

import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;

public interface AnswerNativeRepository {
    Page<AnswerEntity> findAllWithPaging(PagingConditionModel pagingConditionModel);
}
