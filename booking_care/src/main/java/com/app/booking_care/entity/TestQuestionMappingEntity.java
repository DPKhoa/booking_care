package com.app.booking_care.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "test_question_mapping")
@Getter
@Setter
public class TestQuestionMappingEntity extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_id")
    private Long testId;

    @Column(name = "question_id")
    private Long questionId;
}
