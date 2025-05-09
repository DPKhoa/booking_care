package com.app.booking_care.service.Impl;

import com.app.booking_care.constant.AppMessageConstant;
import com.app.booking_care.entity.AttemptDetailEntity;
import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.entity.TestQuestionMappingEntity;
import com.app.booking_care.exception.AppException;
import com.app.booking_care.exception.InvalidInputException;
import com.app.booking_care.model.ErrorModel;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.model.dto.TestSubmission;
import com.app.booking_care.model.dto.UserAnswer;
import com.app.booking_care.repository.*;
import com.app.booking_care.service.TestAttemptService;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestAttemptServiceImpl extends CommonServiceImpl<TestAttemptEntity,Long,TestAttemptRepository> implements TestAttemptService {

    private final AttemptDetailRepository attemptDetailRepository;
    private final TestQuestionMappingRepository testQuestionMappingRepository;
    private final UserRepository userRepository;
    private static final int BATCH_SIZE = 50; // Kích thước lô cho batch processing
    private final HealthTestRepository healthTestRepository;

    public TestAttemptServiceImpl(TestAttemptRepository repo, AttemptDetailRepository attemptDetailRepository, TestQuestionMappingRepository testQuestionMappingRepository, UserRepository userRepository, HealthTestRepository healthTestRepository) {
        super(repo);

        this.attemptDetailRepository = attemptDetailRepository;
        this.testQuestionMappingRepository = testQuestionMappingRepository;
        this.userRepository = userRepository;
        this.healthTestRepository = healthTestRepository;
    }



    //create
    @Override
    @Transactional
    public TestAttemptEntity saveTestAttempt(TestSubmission submission) {

        Logger log = LoggerFactory.getLogger(TestAttemptServiceImpl.class);

        healthTestRepository.findById(submission.getTestId()).orElseThrow(()-> {
            String errorMessage = String.format(
                    AppMessageConstant.ENTITY_NOT_FOUND.getMessage(),
                    submission.getUserId()
            );
            log.error(errorMessage);
            return AppException.of(AppMessageConstant.ENTITY_NOT_FOUND);
        } );
        //Kiểm tra người dùng
        if(userRepository.existsById(submission.getUserId())){
            String errorMessage = String.format(
                    AppMessageConstant.USER_NOT_FOUND.getMessage(),
                    submission.getUserId()
            );

            log.error(errorMessage);
            throw new InvalidInputException(AppMessageConstant.USER_NOT_FOUND, submission.getUserId());
        };

        // Kiểm tra answers có null hoặc rỗng không
        if (submission.getAnswers() == null || submission.getAnswers().isEmpty()) {
            String errorMessage = "Answers cannot be null or empty";
            log.error(errorMessage);
            throw new InvalidInputException(ErrorModel.of(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }
        // Kiểm tra trùng lặp questionId trong answers
        Set<Long> questionIds = new HashSet<>();
        List<Long> duplicateQuestionIds = submission.getAnswers().stream()
                .map(UserAnswer::getQuestionId)
                .filter(questionId -> !questionIds.add(questionId))
                .toList();

        if (!duplicateQuestionIds.isEmpty()) {
            String duplicateIds = duplicateQuestionIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            String errorMessage = String.format(AppMessageConstant.DUPLICATE_QUESTION_ID.getMessage(), duplicateIds);
            log.error(errorMessage);
            throw new InvalidInputException(AppMessageConstant.DUPLICATE_QUESTION_ID, duplicateIds);
        }

        //Kiểm tra các questionId có thuộc testID không?
        long testId = submission.getTestId();
        // Bước 2: Lấy danh sách các questionId hợp lệ
        List<TestQuestionMappingEntity> mappings = testQuestionMappingRepository.findAllByTestId(testId);
        Set<Long> validQuestionIds = mappings.stream()
                .map(TestQuestionMappingEntity::getQuestionId)
                .collect(Collectors.toSet());

        // Bước 2: Tìm các questionId không hợp lệ
        List<UserAnswer> invalidAnswers = submission.getAnswers().stream()
                .filter(userAnswer -> !validQuestionIds.contains(userAnswer.getQuestionId()))
                .toList();

        if (!invalidAnswers.isEmpty()) {
            String invalidQuestionIds = invalidAnswers.stream()
                    .map(userAnswer -> userAnswer.getQuestionId().toString())
                    .collect(Collectors.joining(", "));
            String errorMessage = String.format(
                    AppMessageConstant.INVALID_QUESTION_ID.getMessage(),
                    invalidQuestionIds,
                    testId
            );
            log.error(errorMessage);
            throw new InvalidInputException(AppMessageConstant.INVALID_QUESTION_ID, invalidQuestionIds, testId);
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
