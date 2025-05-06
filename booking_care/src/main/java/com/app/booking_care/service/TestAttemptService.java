package com.app.booking_care.service;

import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.model.dto.TestSubmission;

public interface TestAttemptService extends  CommonService<TestAttemptEntity, Long> {
    TestAttemptEntity saveTestAttempt(TestSubmission submission);
    TestAttemptEntity update(Long id, TestAttemptEntity testAttemptEntity);
}
