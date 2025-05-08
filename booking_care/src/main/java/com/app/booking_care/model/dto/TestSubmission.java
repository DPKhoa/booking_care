package com.app.booking_care.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class TestSubmission {
    private Long testId;
    private Long userId;
    private LocalDateTime attemptDate;
    private List<UserAnswer> answers;
}

