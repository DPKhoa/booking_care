package com.app.booking_care.repository;

import com.app.booking_care.entity.TestQuestionMappingEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestQuestionMappingRepository extends CommonJpaRepository<TestQuestionMappingEntity,Long> {
    @Query("SELECT tqm FROM TestQuestionMappingEntity tqm WHERE tqm.testId = :testId")
    List<TestQuestionMappingEntity> findAllByTestId(@Param("testId") Long testId);
}
