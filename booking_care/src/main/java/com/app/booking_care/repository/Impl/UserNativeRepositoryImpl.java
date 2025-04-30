package com.app.booking_care.repository.Impl;

import com.app.booking_care.component.NativeQueryComponent;
import com.app.booking_care.entity.UserEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.UserNativeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class UserNativeRepositoryImpl implements UserNativeRepository {
    private final NativeQueryComponent queryComponent;

    public UserNativeRepositoryImpl(NativeQueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    @Override
    public Page<UserEntity> findAllWithPaging(PagingConditionModel pagingConditionModel) {
        String sql = """
                SELECT * FROM USER
               """;
        String countSql = "SELECT COUNT(U.ID) FROM USER U";

        Object[] params = new Object[]{};
        Pageable pageable = PageRequest.of(pagingConditionModel.getPageCurrent(), pagingConditionModel.getPageSize());
        return queryComponent.findPage(sql,countSql,pageable, UserEntity.class,params);
    }
}
