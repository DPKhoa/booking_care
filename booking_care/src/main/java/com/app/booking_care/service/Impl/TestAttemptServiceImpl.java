package com.app.booking_care.service.Impl;

import com.app.booking_care.entity.AttemptDetailEntity;
import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.model.dto.TestSubmission;
import com.app.booking_care.model.dto.UserAnswer;
import com.app.booking_care.repository.AttemptDetailRepository;
import com.app.booking_care.repository.TestAttemptRepository;
import com.app.booking_care.service.TestAttemptService;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestAttemptServiceImpl extends CommonServiceImpl<TestAttemptEntity,Long,TestAttemptRepository> implements TestAttemptService {

    private final AttemptDetailRepository attemptDetailRepository;

    public TestAttemptServiceImpl(TestAttemptRepository repo, TestAttemptRepository testAttemptRepository, AttemptDetailRepository attemptDetailRepository) {
        super(repo);

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
        testAttemptEntity = getRepo().save(testAttemptEntity);


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
        testAttemptEntity = getRepo().save(testAttemptEntity);
        return testAttemptEntity;
    }

    @Override
    public TestAttemptEntity save(TestAttemptEntity entity) {
        return getRepo().save(entity);
    }

    @Override
    public TestAttemptEntity getById(Long aLong) throws Exception {
        return null;
    }

    @Override
    public List<TestAttemptEntity> getAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void deleteByIdIn(List<Long> longs) {

    }

    @Override
    public Page<TestAttemptEntity> getAllWithPaging(PagingConditionModel pagingConditionModel) {
        return null;
    }
}
