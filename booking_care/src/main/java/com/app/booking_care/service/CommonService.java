package com.app.booking_care.service;

import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommonService<T,ID> {
    T save(T entity);
    T getById(ID id) throws Exception;
    List<T> getAll();
    void deleteById(ID id);
    boolean existsById(ID id);
    void deleteByIdIn(List<ID> ids);
    Page<T> getAllWithPaging(PagingConditionModel pagingConditionModel);

}
