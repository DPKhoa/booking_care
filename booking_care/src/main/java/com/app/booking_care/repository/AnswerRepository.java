package com.app.booking_care.repository;

import com.app.booking_care.entity.AnswerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AnswerRepository extends CommonJpaRepository<AnswerEntity, Long>, AnswerNativeRepository {
    @Query("SELECT a FROM AnswerEntity a WHERE a.questionId IN :questionIds")
    List<AnswerEntity> findAllByQuestionIds(@Param("questionIds") List<Long> questionIds);

}
