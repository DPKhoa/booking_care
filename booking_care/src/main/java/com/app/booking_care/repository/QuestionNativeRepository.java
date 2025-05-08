package com.app.booking_care.repository;


import com.app.booking_care.entity.QuestionEntity;
import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;


public interface QuestionNativeRepository {
    Page<QuestionEntity> findAllWithPaging(PagingConditionModel pagingConditionModel);
}
