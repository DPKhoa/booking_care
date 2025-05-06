package com.app.booking_care.repository;

import com.app.booking_care.entity.AttemptDetailEntity;

public interface AttemptDetailRepository extends CommonJpaRepository<AttemptDetailEntity,Long>,AttemptDetailNativeRepository {
    void deleteByAttemptId(Long id);
}
