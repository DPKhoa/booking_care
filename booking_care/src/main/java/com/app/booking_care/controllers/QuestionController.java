package com.app.booking_care.controllers;

import com.app.booking_care.entity.QuestionEntity;
import com.app.booking_care.model.PagingConditionModel;
import com.app.booking_care.service.QuestionService;
import com.app.booking_care.util.UrlUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlUtil.QUESTION_URL)
public class QuestionController extends CommonController<QuestionEntity, Long, QuestionService>{
    public QuestionController(QuestionService service) {
        super(service);
    }

    @PostMapping()
    public ResponseEntity<QuestionEntity> create (@RequestBody QuestionEntity questionEntity){
        return ResponseEntity.ok(getService().save(questionEntity));
    }

    @GetMapping("/paging")
    public Page<QuestionEntity> findAllWithPaging(@RequestBody PagingConditionModel pagingConditionModel) {
        return getService().getAllWithPaging(pagingConditionModel);
    }
    @GetMapping()
    public List<QuestionEntity> getAll(){
        return getService().getAll();
    }
    @PutMapping("/{id}")
    public ResponseEntity<QuestionEntity> update (@PathVariable Long id, @RequestBody QuestionEntity questionEntity){
        return ResponseEntity.ok(getService().update(id, questionEntity));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
