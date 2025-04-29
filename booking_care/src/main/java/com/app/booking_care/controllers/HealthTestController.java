package com.app.booking_care.controllers;

import com.app.booking_care.entity.HealthTestEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.service.HealthTestService;
import com.app.booking_care.util.UrlUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlUtil.HEALTH_TEST_URL)
public class HealthTestController extends CommonController<HealthTestEntity,Long, HealthTestService> {
    public HealthTestController(HealthTestService service) {
        super(service);
    }

    @GetMapping("/paging")
    public Page<HealthTestEntity> findAllWithPaging(@RequestBody PagingConditionModel pagingConditionModel) {
        return getService().getAllWithPaging(pagingConditionModel);
    }
    @GetMapping()
    public List<HealthTestEntity> getAll(){
        return getService().getAll();
    }
    @PostMapping()
    public ResponseEntity<HealthTestEntity> create (@RequestBody HealthTestEntity healthTestEntity){
        return ResponseEntity.ok(getService().save(healthTestEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthTestEntity> update (@PathVariable Long id, @RequestBody HealthTestEntity healthTestEntity){
        return ResponseEntity.ok(getService().update(id, healthTestEntity));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
