package com.app.booking_care.service.Impl;

import com.app.booking_care.entity.AttemptDetailEntity;
import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.model.dto.TestSubmission;
import com.app.booking_care.model.dto.UserAnswer;
import com.app.booking_care.repository.AttemptDetailRepository;
import com.app.booking_care.repository.TestAttemptRepository;
import com.app.booking_care.service.TestAttemptService;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class TestAttemptServiceImpl implements TestAttemptService {
    private final TestAttemptRepository testAttemptRepository;
    private final AttemptDetailRepository attemptDetailRepository;
    public TestAttemptServiceImpl(TestAttemptRepository testAttemptRepository, AttemptDetailRepository attemptDetailRepository) {
        this.testAttemptRepository = testAttemptRepository;
        this.attemptDetailRepository = attemptDetailRepository;
    }


    @Override
    @Transactional
    public TestAttemptEntity saveTestAttempt(TestSubmission submission) {
        // tạo và lưu TestAttempt
        TestAttemptEntity testAttemptEntity = new TestAttemptEntity();
        testAttemptEntity.setTestId(submission.getTestId());
        testAttemptEntity.setUserId(submission.getUserId());
        testAttemptEntity.setAttemptDate(submission.getAttemptDate());
        testAttemptEntity.setTotalScore(0L);
        testAttemptEntity.setStatus("COMPLETED");
        testAttemptEntity = testAttemptRepository.save(testAttemptEntity);


        // Bước 2: Lưu chi tiết bài làm vào AttemptDetail
        Long attemptId = testAttemptEntity.getId();
        for (UserAnswer userAnswer : submission.getAnswers()) {
            AttemptDetailEntity attemptDetail = new AttemptDetailEntity();
            attemptDetail.setAttemptId(attemptId);
            attemptDetail.setQuestionId(userAnswer.getQuestionId());
            attemptDetail.setAnswerId(userAnswer.getAnswerId());
            AttemptDetailEntity save = attemptDetailRepository.save(attemptDetail);

        }
        // Bước 3: Tính tổng điểm
        Long totalScore = attemptDetailRepository.calculateTotalScore(attemptId);

        // Bước 4: Cập nhật total_score trong TestAttempt
        testAttemptEntity.setTotalScore(totalScore);
        testAttemptEntity = testAttemptRepository.save(testAttemptEntity);
        return testAttemptEntity;
    }
}
