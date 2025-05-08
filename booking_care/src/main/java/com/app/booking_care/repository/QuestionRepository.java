package com.app.booking_care.repository;

import com.app.booking_care.entity.QuestionEntity;

public interface QuestionRepository extends CommonJpaRepository<QuestionEntity, Long>, QuestionNativeRepository {
}
