package com.app.booking_care.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_attempt")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAttemptEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_id")
    private Long testId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "attempt_date")
    private LocalDateTime attemptDate;

    @Column(name = "total_score")
    private Long totalScore;

    @Column(name = "status")
    private String status;
    private Double score; // Tổng điểm
    private String result; // Tình trạng sức khỏe
    private Integer totalQuestions; // Tổng số câu hỏi
}
