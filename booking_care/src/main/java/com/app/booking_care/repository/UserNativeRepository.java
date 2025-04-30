package com.app.booking_care.repository;


import com.app.booking_care.entity.UserEntity;
import com.app.booking_care.model.PagingConditionModel;
import org.springframework.data.domain.Page;

public interface UserNativeRepository {
    Page<UserEntity> findAllWithPaging(PagingConditionModel pagingConditionModel);
}
