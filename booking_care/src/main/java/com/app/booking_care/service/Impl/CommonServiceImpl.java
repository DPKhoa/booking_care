package com.app.booking_care.service.Impl;

import com.app.booking_care.constant.AppMessageConstant;
import com.app.booking_care.exception.AppException;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.CommonJpaRepository;
import com.app.booking_care.service.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class CommonServiceImpl<T,ID, R extends CommonJpaRepository<T,ID>> implements CommonService<T,ID>{
    private final R repo;

    public CommonServiceImpl(R repo) {
        this.repo = repo;
    }

    @Override
    public T save(T entity) {
       return repo.save(entity);
    }

    @Override
    public T getById(ID id) throws Exception {
        Optional<T> entityOptional = repo.findById(id);
        if (!entityOptional.isPresent()) {
            throw AppException.of(AppMessageConstant.ENTITY_NOT_FOUND);
        }
        return entityOptional.get();
    }

    @Override
    public List<T> getAll() {
        return repo.findAll();
    }

    @Override
    public void deleteById(ID id) {
        if(!existsById(id)) throw new AppException(AppMessageConstant.ENTITY_NOT_FOUND);
        repo.deleteById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return repo.existsById(id);
    }

    @Override
    public void deleteByIdIn(List<ID> ids) {
    repo.deleteByIdIn(ids);
    }

    @Override
    public Page<T> getAllWithPagingUsingJpa(PagingConditionModel pagingConditionModel) {
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return repo.findAll(pageable);
    }

}
