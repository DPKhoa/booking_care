package com.app.booking_care.repository;

import com.app.booking_care.entity.UserEntity;

public interface UserRepository extends CommonJpaRepository<UserEntity, Long>, UserNativeRepository {
}
