package com.app.booking_care.repository;

import com.app.booking_care.entity.HealthTestEntity;

public interface HealthTestRepository extends CommonJpaRepository<HealthTestEntity, Long>, HealthTestNativeRepository {
}
