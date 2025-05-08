package com.app.booking_care.service.Impl;

import com.app.booking_care.entity.HealthTestEntity;

import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.HealthTestRepository;
import com.app.booking_care.service.HealthTestService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthTestServiceImpl extends CommonServiceImpl<HealthTestEntity,Long, HealthTestRepository> implements HealthTestService {
    public HealthTestServiceImpl(HealthTestRepository repo) {
        super(repo);
    }

    @Override
    public HealthTestEntity save(HealthTestEntity entity) {
        return getRepo().save(entity);
    }

    @Override
    public HealthTestEntity getById(Long id) throws Exception {
        return getRepo().getById(id);
    }

    @Override
    public List<HealthTestEntity> getAll() {
        return getRepo().findAll();
    }

    @Override
    public void deleteById(Long id) {
        HealthTestEntity healthTest = getRepo().findById(id).orElseThrow(() -> new RuntimeException("Test not found with id: " + id));
        getRepo().delete(healthTest);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void deleteByIdIn(List<Long> longs) {

    }

    @Override
    public Page<HealthTestEntity> getAllWithPaging(PagingConditionModel pagingConditionModel) {
        return getRepo().findAllWithPaging(pagingConditionModel);
    }

    @Override
    public HealthTestEntity update(Long id, HealthTestEntity healthTest) {
        HealthTestEntity healthTestEntity = getRepo().findById(id).orElseThrow(() -> new RuntimeException("Test not found with id: " + id));
        healthTestEntity.setTest_name(healthTest.getTest_name());
        healthTestEntity.setDuration(healthTest.getDuration());
        healthTestEntity.setDescription(healthTest.getDescription());
        healthTestEntity.setStatus(healthTest.getStatus());
        return getRepo().save(healthTestEntity);
    }
}
