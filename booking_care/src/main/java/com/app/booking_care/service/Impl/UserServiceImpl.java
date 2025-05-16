package com.app.booking_care.service.Impl;

import com.app.booking_care.entity.UserEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.repository.UserRepository;
import com.app.booking_care.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends CommonServiceImpl<UserEntity,Long, UserRepository> implements UserService {
    public UserServiceImpl(UserRepository repo) {
        super(repo);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return getRepo().save(entity);
    }

    @Override
    public UserEntity getById(Long id) throws Exception {
        return getRepo().findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<UserEntity> getAll() {
        return getRepo().findAll();
    }

    @Override
    public void deleteById(Long id) {
        UserEntity user = getRepo().findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        getRepo().delete(user);
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false; // Hoặc ném ngoại lệ tùy theo yêu cầu
        }
        return getRepo().existsById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> longs) {

    }

    @Override
    public Page<UserEntity> getAllWithPaging(PagingConditionModel pagingConditionModel) {
        return getRepo().findAllWithPaging(pagingConditionModel);
    }

    @Override
    public UserEntity update(Long id, UserEntity user) {
        UserEntity userEntity = getRepo().findById(id).orElseThrow(()->new RuntimeException("User not found with id: " + id));
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setUsername(user.getUsername());
        return getRepo().save(userEntity);
    }
}
