package com.app.booking_care.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CommonJpaRepository<T,ID> extends JpaRepository<T,ID> {
    void deleteByIdIn(List<ID> ids);
}
