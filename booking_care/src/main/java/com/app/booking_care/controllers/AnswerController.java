package com.app.booking_care.controllers;

import com.app.booking_care.entity.AnswerEntity;

import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.service.AnswerService;
import com.app.booking_care.util.UrlUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlUtil.ANSWER_URL)
public class AnswerController extends CommonController<AnswerEntity,Long, AnswerService> {

    public AnswerController(AnswerService service) {
        super(service);
    }
    @PostMapping()
    public ResponseEntity<AnswerEntity> create(@RequestBody AnswerEntity answer){
        return ResponseEntity.ok(getService().save(answer));

    }
    @GetMapping()
    public List<AnswerEntity> getAll(){
        return getService().getAll();
    }
    @GetMapping("/paging")
    public Page<AnswerEntity> findAllWithPaging(@RequestBody PagingConditionModel pagingConditionModel) {
        return getService().getAllWithPaging(pagingConditionModel);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AnswerEntity> update(@PathVariable Long id, @RequestBody AnswerEntity answer){
        return ResponseEntity.ok(getService().update(id, answer));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
