package com.app.booking_care.service;

import com.app.booking_care.entity.AnswerEntity;

public interface AnswerService extends CommonService<AnswerEntity, Long> {
    AnswerEntity update(Long id,AnswerEntity answer);
}
