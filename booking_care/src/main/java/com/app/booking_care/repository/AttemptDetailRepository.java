package com.app.booking_care.repository;

import com.app.booking_care.entity.AttemptDetailEntity;
import org.springframework.data.jpa.repository.Query;

public interface AttemptDetailRepository extends CommonJpaRepository<AttemptDetailEntity,Long>,AttemptDetailNativeRepository {
    void deleteByAttemptId(Long id);
    @Query("SELECT SUM(a.score) FROM AnswerEntity a JOIN AttemptDetailEntity ad ON a.id = ad.answerId WHERE ad.attemptId = :attemptId")
    Long calculateTotalScore(Long attemptId);
}
