package com.app.booking_care.service;

import com.app.booking_care.entity.HealthTestEntity;

public interface HealthTestService extends CommonService<HealthTestEntity, Long> {
    HealthTestEntity update(Long id, HealthTestEntity healthTest);
}
