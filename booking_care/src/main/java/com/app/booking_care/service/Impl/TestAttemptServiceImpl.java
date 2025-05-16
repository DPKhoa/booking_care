package com.app.booking_care.service.Impl;

import com.app.booking_care.constant.AppMessageConstant;
import com.app.booking_care.entity.AnswerEntity;
import com.app.booking_care.entity.AttemptDetailEntity;
import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.entity.TestQuestionMappingEntity;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestAttemptServiceImpl extends CommonServiceImpl<TestAttemptEntity,Long,TestAttemptRepository> implements TestAttemptService {

    private final AttemptDetailRepository attemptDetailRepository;
    private final TestQuestionMappingRepository testQuestionMappingRepository;
    private final UserRepository userRepository;
    private static final int BATCH_SIZE = 50; // Kích thước lô cho batch processing
    private final HealthTestRepository healthTestRepository;
    private final AnswerRepository answerRepository;

    public TestAttemptServiceImpl(TestAttemptRepository repo, AttemptDetailRepository attemptDetailRepository, TestQuestionMappingRepository testQuestionMappingRepository, UserRepository userRepository, HealthTestRepository healthTestRepository, AnswerRepository answerRepository) {
        super(repo);

        this.attemptDetailRepository = attemptDetailRepository;
        this.testQuestionMappingRepository = testQuestionMappingRepository;
        this.userRepository = userRepository;
        this.healthTestRepository = healthTestRepository;
        this.answerRepository = answerRepository;
    }

    Logger log = LoggerFactory.getLogger(TestAttemptServiceImpl.class);

    private void validateTestSubmission(TestSubmission testSubmission) {
        if (testSubmission.getUserId() == null) {
            throw new InvalidInputException(ErrorModel.of(HttpStatus.BAD_REQUEST.value(), "User ID cannot be null"));
        }
        if (testSubmission.getTestId() == null) {
            throw new InvalidInputException(ErrorModel.of(HttpStatus.BAD_REQUEST.value(), "Test ID cannot be null"));
        }
        if (testSubmission.getAnswers() == null || testSubmission.getAnswers().isEmpty()) {
            throw new InvalidInputException(ErrorModel.of(HttpStatus.BAD_REQUEST.value(), "Answers cannot be null or empty"));
        }
        if (testSubmission.getAttemptDate() != null && testSubmission.getAttemptDate().isAfter(LocalDateTime.now())) {
            throw new InvalidInputException(ErrorModel.of(HttpStatus.BAD_REQUEST.value(), "Attempt date cannot be in the future"));
        }
    }

    //create
    @Override
    @Transactional
    public TestAttemptEntity saveTestAttempt(TestSubmission submission) {
        if(submission == null){
            throw new InvalidInputException(ErrorModel.of(HttpStatus.BAD_REQUEST.value(), "Test submission cannot be null"));
        }
        validateTestSubmission(submission);

        if(!healthTestRepository.existsById(submission.getTestId())) {
            String errorMessage = String.format(
                    AppMessageConstant.ENTITY_NOT_FOUND.getMessage(),
                    submission.getTestId()
            );
            log.error(errorMessage);
            throw new InvalidInputException(AppMessageConstant.USER_NOT_FOUND, submission.getTestId());
        }
        //Kiểm tra người dùng
        if(!userRepository.existsById(submission.getUserId())){
            String errorMessage = String.format(
                    AppMessageConstant.USER_NOT_FOUND.getMessage(),
                    submission.getUserId()
            );

            log.error(errorMessage);
            throw new InvalidInputException(AppMessageConstant.USER_NOT_FOUND, submission.getUserId());
        };

        // Lấy mappings và kiểm tra
        List<TestQuestionMappingEntity> mappings = testQuestionMappingRepository.findAllByTestId(submission.getTestId());
        if (mappings == null || mappings.isEmpty()) {
            throw new InvalidInputException(AppMessageConstant.TEST_NOT_FOUND, submission.getTestId());
        }


        // Kiểm tra tính hợp lệ của questionId
        Set<Long> validQuestionIds = mappings.stream()
                .map(TestQuestionMappingEntity::getQuestionId)
                .collect(Collectors.toSet());
        List<UserAnswer> invalidAnswers = submission.getAnswers().stream()
                .filter(userAnswer -> userAnswer == null || !validQuestionIds.contains(userAnswer.getQuestionId()))
                .toList();
        if (!invalidAnswers.isEmpty()) {
            String invalidQuestionIds = invalidAnswers.stream()
                    .filter(Objects::nonNull)
                    .map(userAnswer -> userAnswer.getQuestionId().toString())
                    .collect(Collectors.joining(", "));
            String errorMessage = String.format(AppMessageConstant.INVALID_QUESTION_ID.getMessage(), invalidQuestionIds, submission.getTestId());
            log.error(errorMessage);
            throw new InvalidInputException(AppMessageConstant.INVALID_QUESTION_ID, invalidQuestionIds, submission.getTestId());
        }

        // Lưu TestAttemptEntity
        TestAttemptEntity testAttempt = new TestAttemptEntity();
        testAttempt.setTestId(submission.getTestId());
        testAttempt.setUserId(submission.getUserId());
        testAttempt.setAttemptDate(LocalDateTime.now());
        testAttempt = getRepo().save(testAttempt);
        Long attemptId = testAttempt.getId();

        log.info("Saved TestAttemptEntity for userId={} with attemptId={} at {}",
                submission.getUserId(), attemptId, testAttempt.getAttemptDate());
        // Lưu AttemptDetails và tính điểm sức khỏe
        try {
            return saveAttemptDetail(attemptId, mappings, submission.getAnswers());
        } catch (Exception e) {
            log.error("Failed to save attempt details for attemptId={} due to: {}", attemptId, e.getMessage(), e);
            throw new InvalidInputException(AppMessageConstant.INTERNAL_SERVER_ERROR, "Failed to process attempt details");
        }
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
    private void validateAnswerId(Map<Long, Long> answerMap, Map<Long, Long> answerScores){
        List<Long> invalidAnswerId = answerMap.values().stream().filter(answerId -> !answerScores.containsKey(answerId)).toList();
        if(!invalidAnswerId.isEmpty()){
            String invalidIds = invalidAnswerId.stream().map(String:: valueOf).collect(Collectors.joining(", "));
            throw new InvalidInputException(AppMessageConstant.INVALID_ANSWER_ID, invalidIds);
        }
    }
    /*
    * Phân loại tình trạng sức khỏe theo thang BDI
    * */

    private String calculateHealthStatus(int totalScore){
        if(totalScore <= 13){
            return "BÌNH THƯỜNG";
        }
        else if(totalScore <= 19){
            return "NHẸ";
        }
        else if(totalScore <= 28){
            return "TRUNG BÌNH";
        }
        else{
            return "NẶNG";
        }
    }
    // Lấy điểm của từng answerId từ mappings
    private Map<Long, Long> getAnswersScore(List<TestQuestionMappingEntity> mappings){
        // Lấy questionId từ TestQuestionMapping
        List<Long> questionId = mappings.stream().map(TestQuestionMappingEntity::getQuestionId).toList();
        List<AnswerEntity> answers  = answerRepository.findAllByQuestionIds(questionId);
        return answers.stream().collect(Collectors.toMap(AnswerEntity::getId, answer -> answer.getScore() != null ? answer.getScore() : 0L,(existing,newvValue)-> existing));

    }
  @Transactional
    public TestAttemptEntity saveAttemptDetail(Long attemptId, List<TestQuestionMappingEntity> mappings, List<UserAnswer> userAnswers) {

      //Kiem tra du lieu dau vao
        if(mappings == null || mappings.isEmpty()) {
            throw new InvalidInputException(ErrorModel.of(HttpStatus.BAD_REQUEST.value()));
        }
        // Ánh xạ questionId tới answerId( lấy giá trị cuối nếu trùng lặp)
      Map<Long, Long> answerMap = userAnswers.stream().collect(Collectors.toMap(UserAnswer:: getQuestionId, UserAnswer:: getAnswerId, (existing, newValue)-> newValue));
        // lá tất cả các answerId  hợp lệ và tính điểm
      Map<Long, Long> answerScore = getAnswersScore(mappings);
      validateAnswerId(answerMap, answerScore);

      List<AttemptDetailEntity> attemptDetails = new ArrayList<>();
      int totalQuestions = mappings.size();
      int totalScore = 0;
      int count = 0;
      for(TestQuestionMappingEntity mapping : mappings) {
          AttemptDetailEntity attemptDetail  = new AttemptDetailEntity();
          attemptDetail.setAttemptId(attemptId);
          attemptDetail.setQuestionId(mapping.getQuestionId());
          Long userAnswerId = answerMap.get(mapping.getQuestionId());
          attemptDetail.setAnswerId(userAnswerId);
// Tính điểm dựa trên answerId
          if (userAnswerId != null) {
              Long score = answerScore.get(userAnswerId);
              totalScore += score;
          }

          attemptDetails.add(attemptDetail);
          count++;

          if (count >= BATCH_SIZE) {
              attemptDetailRepository.saveAll(attemptDetails);
              attemptDetailRepository.flush();
              attemptDetails.clear();
              count = 0;
          }

      }
      if (!attemptDetails.isEmpty()) {
          attemptDetailRepository.saveAll(attemptDetails);
          attemptDetailRepository.flush();
      }
      // Phân loại tình trạng sức khỏe (theo thang BDI)
      String healthStatus = calculateHealthStatus(totalScore);
      log.info("User health assessment: Total score = {}, Status = {}, Total questions = {}", totalScore, healthStatus, totalQuestions);

      // Lưu kết quả vào TestAttemptEntity
      TestAttemptEntity testAttempt = getRepo().findById(attemptId)
              .orElseThrow(() -> new InvalidInputException(AppMessageConstant.ENTITY_NOT_FOUND, attemptId));
      testAttempt.setScore((double) totalScore);
      testAttempt.setResult(healthStatus);
      testAttempt.setTotalQuestions(totalQuestions); // Thêm trường để lưu tổng số câu hỏi
      getRepo().save(testAttempt);


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
