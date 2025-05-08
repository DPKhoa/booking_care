package com.app.booking_care.controllers;


import com.app.booking_care.entity.UserEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.service.UserService;
import com.app.booking_care.util.UrlUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlUtil.USER_URL)
public class UserController extends CommonController<UserEntity,Long, UserService> {
    public UserController(UserService service) {
        super(service);
    }
    @PostMapping()
    public ResponseEntity<UserEntity> create (@RequestBody UserEntity userEntity){
        return ResponseEntity.ok(getService().save(userEntity));
    }

    @GetMapping("/paging")
    public Page<UserEntity> findAllWithPaging(@RequestBody PagingConditionModel pagingConditionModel) {
        return getService().getAllWithPaging(pagingConditionModel);
    }
    @GetMapping()
    public List<UserEntity> getAll(){
        return getService().getAll();
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> update (@PathVariable Long id, @RequestBody UserEntity userEntity){
        return ResponseEntity.ok(getService().update(id, userEntity));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
