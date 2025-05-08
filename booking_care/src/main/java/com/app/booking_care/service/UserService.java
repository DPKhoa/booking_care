package com.app.booking_care.service;

import com.app.booking_care.entity.UserEntity;

public interface UserService extends CommonService<UserEntity, Long>{
    UserEntity update(Long id, UserEntity user);

}
