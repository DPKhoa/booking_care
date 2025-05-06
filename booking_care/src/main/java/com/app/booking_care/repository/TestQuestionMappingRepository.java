package com.app.booking_care.repository;

import com.app.booking_care.entity.TestQuestionMappingEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestQuestionMappingRepository extends CommonJpaRepository<TestQuestionMappingEntity,Long>, TestQuestionMappingNativeRepository {
    @Query("SELECT tqm.questionId FROM TestQuestionMappingEntity tqm WHERE tqm.testId = :testId AND tqm.questionId NOT IN :questionIds")
    List<TestQuestionMappingEntity> findAllByTestId(@Param("testId") Long testId);
}
