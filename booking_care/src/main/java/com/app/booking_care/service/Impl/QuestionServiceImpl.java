package com.app.booking_care.service.Impl;

import com.app.booking_care.entity.QuestionEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.QuestionRepository;
import com.app.booking_care.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

        return new ArrayList<>(getRepo().findAll());
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
    public Page<QuestionEntity> getAllWithPagingUsingJpa(PagingConditionModel pagingConditionModel) {
        return null;
    }
}
