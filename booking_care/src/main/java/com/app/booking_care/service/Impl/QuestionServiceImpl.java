package com.app.booking_care.service.Impl;

import com.app.booking_care.entity.QuestionEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.QuestionRepository;
import com.app.booking_care.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class QuestionServiceImpl extends CommonServiceImpl<QuestionEntity, Long, QuestionRepository> implements QuestionService {
    public QuestionServiceImpl(QuestionRepository repo) {
        super(repo);
    }

    @Override
    public QuestionEntity save(QuestionEntity entity) {
        return getRepo().save(entity);
    }

    @Override
    public QuestionEntity getById(Long id) throws Exception {
        return getRepo().findById(id).orElseThrow(() -> new RuntimeException("Question id not found"));
    }

    @Override
    public List<QuestionEntity> getAll() {

        return getRepo().findAll();
    }

    @Override
    public void deleteById(Long id) {
        QuestionEntity question = getRepo().findById(id).orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        getRepo().delete(question);
    }

    @Override
    public boolean existsById(Long questionId) {

        return false;
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {

    }

    @Override
    public Page<QuestionEntity> getAllWithPaging(PagingConditionModel pagingConditionModel) {
        return getRepo().findAllWithPaging(pagingConditionModel);
    }

    @Override
    public QuestionEntity update(Long id, QuestionEntity question) {
        QuestionEntity questionEntity = getRepo().findById(id).orElseThrow(()-> new RuntimeException("Answer is not found with id" + id) );
        questionEntity.setContent(question.getContent());
        return getRepo().save(question);
    }
}
