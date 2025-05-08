package com.app.booking_care.service.Impl;

import com.app.booking_care.constant.AppMessageConstant;
import com.app.booking_care.entity.AttemptDetailEntity;
import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.entity.TestQuestionMappingEntity;
import com.app.booking_care.entity.UserEntity;
import com.app.booking_care.exception.AppException;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.model.dto.TestSubmission;
import com.app.booking_care.model.dto.UserAnswer;
import com.app.booking_care.repository.AttemptDetailRepository;
import com.app.booking_care.repository.TestAttemptRepository;
import com.app.booking_care.repository.TestQuestionMappingRepository;
import com.app.booking_care.repository.UserRepository;
import com.app.booking_care.service.TestAttemptService;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestAttemptServiceImpl extends CommonServiceImpl<TestAttemptEntity,Long,TestAttemptRepository> implements TestAttemptService {

    private final AttemptDetailRepository attemptDetailRepository;
    private final TestQuestionMappingRepository testQuestionMappingRepository;
    private final UserRepository userRepository;
    private static final int BATCH_SIZE = 50; // Kích thước lô cho batch processing

    public TestAttemptServiceImpl(TestAttemptRepository repo, AttemptDetailRepository attemptDetailRepository, TestQuestionMappingRepository testQuestionMappingRepository, UserRepository userRepository) {
        super(repo);

        this.attemptDetailRepository = attemptDetailRepository;
        this.testQuestionMappingRepository = testQuestionMappingRepository;
        this.userRepository = userRepository;
    }



    //create
    @Override
    @Transactional
    public TestAttemptEntity saveTestAttempt(TestSubmission submission) {

        //Kiểm tra người dùng
        userRepository.findById(submission.getUserId()).orElseThrow(()-> {
            String errorMessage = String.format(
                    AppMessageConstant.ENTITY_NOT_FOUND.getMessage(),
                    submission.getUserId()
            );

            return AppException.of(AppMessageConstant.ENTITY_NOT_FOUND);
        });
        //Kiểm tra các questionId có thuộc testID không?
        long testId = submission.getTestId();
        // Bước 2: Lấy danh sách các questionId hợp lệ
        List<TestQuestionMappingEntity> mappings = testQuestionMappingRepository.findAllByTestId(testId);
        Set<Long> validQuestionIds = mappings.stream()
                .map(TestQuestionMappingEntity::getQuestionId)
                .collect(Collectors.toSet());


        List<UserAnswer> invalidAnswers = new ArrayList<>();
        for (UserAnswer userAnswer : submission.getAnswers()) {
            if (!validQuestionIds.contains(userAnswer.getQuestionId())) {
                invalidAnswers.add(userAnswer);
            }
        }
        if (!invalidAnswers.isEmpty()) {
            throw AppException.of(AppMessageConstant.INVALID_QUESTION_ID);
        }
        // Bước 3: Tao luu TestAttempt

        TestAttemptEntity testAttempt = TestAttemptEntity.builder()
                .testId(submission.getTestId())
                .userId(submission.getUserId())
                .attemptDate(submission.getAttemptDate())
                .totalScore(0L)
                .status("COMPLETED")
                .build();
        testAttempt = getRepo().save(testAttempt);

        // Bước 4: Lưu AttemptDetail theo batch
        Long attemptId = testAttempt.getId();
        List<AttemptDetailEntity> attemptDetails = new ArrayList<>();
        for (TestQuestionMappingEntity mapping : mappings) { // Duyệt qua tất cả câu hỏi hợp lệ
            AttemptDetailEntity attemptDetail = new AttemptDetailEntity();
            attemptDetail.setAttemptId(attemptId);
            attemptDetail.setQuestionId(mapping.getQuestionId());

            // Lưu theo batch khi đạt kích thước lô
            if (attemptDetails.size() >= BATCH_SIZE) {
                attemptDetailRepository.saveAll(attemptDetails);
                attemptDetails.clear();
            }
        }
        // Lưu các bản ghi còn lại (nếu có)
        if (!attemptDetails.isEmpty()) {
            attemptDetailRepository.saveAll(attemptDetails);
        }
        // Bước 5: Tính tổng điểm
        Long totalScore = attemptDetailRepository.calculateTotalScore(attemptId);
        if (totalScore == null) {
            totalScore = 0L; // Xử lý trường hợp không có điểm
        }

        // Bước 7: Cập nhật total_score
        testAttempt.setTotalScore(totalScore);
        testAttempt = getRepo().save(testAttempt);
        return testAttempt;
    }

    @Override
    public TestAttemptEntity update(Long id, TestAttemptEntity testAttemptEntity) {
        TestAttemptEntity testAttempt = getRepo().findById(id)
                .orElseThrow(() -> new RuntimeException(String.valueOf(id)));
        testAttempt.setTestId(testAttemptEntity.getTestId());
        testAttempt.setAttemptDate(testAttempt.getAttemptDate());
        testAttempt.setUserId(testAttemptEntity.getUserId());
        testAttempt.setTotalScore(testAttemptEntity.getTotalScore());
        testAttempt.setStatus("COMPLETED");
        testAttempt = getRepo().save(testAttempt);
        return testAttempt;
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
        return getRepo().findAll();
    }

    @Override
    public void deleteById(Long id) {
        TestAttemptEntity testAttempt = getRepo().findById(id).orElseThrow(()-> new RuntimeException("Attempt not found"));
        attemptDetailRepository.deleteByAttemptId(id);
        getRepo().delete(testAttempt);

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
