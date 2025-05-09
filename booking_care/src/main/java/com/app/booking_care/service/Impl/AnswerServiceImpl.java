package com.app.booking_care.service.Impl;

import com.app.booking_care.entity.AnswerEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.AnswerRepository;
import com.app.booking_care.service.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl extends CommonServiceImpl<AnswerEntity,Long, AnswerRepository> implements AnswerService {
    public AnswerServiceImpl(AnswerRepository repo) {
        super(repo);
    }

    @Override
    public AnswerEntity save(AnswerEntity entity) {
        return getRepo().save(entity);
    }

    @Override
    public AnswerEntity getById(Long id) throws Exception {
        return getRepo().findById(id).orElseThrow(() -> new RuntimeException("Answer id not found"));
    }

    @Override
    public List<AnswerEntity> getAll() {
        return getRepo().findAll();
    }

    @Override
    public void deleteById(Long id) {
        AnswerEntity answer = getRepo().findById(id).orElseThrow(() -> new RuntimeException("Answer id not found"));
        getRepo().delete(answer);
    }

    @Override
    public boolean existsById(Long id) {

        return false;
    }

    @Override
    public void deleteByIdIn(List<Long> longs) {

    }



    @Override
    public Page<AnswerEntity> getAllWithPaging(PagingConditionModel pagingConditionModel) {
        return getRepo().findAllWithPaging(pagingConditionModel);
    }

    @Override
    public AnswerEntity update(Long id, AnswerEntity answer) {
        AnswerEntity answerEntity = getRepo().findById(id).orElseThrow(() -> new RuntimeException("Answer id not found" + id));
        answerEntity.setContent(answer.getContent());
        answerEntity.setScore(answer.getScore());
        return getRepo().save(answer);

    }
}
