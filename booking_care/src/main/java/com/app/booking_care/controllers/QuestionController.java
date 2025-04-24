package com.app.booking_care.controllers;

import com.app.booking_care.entity.QuestionEntity;
import com.app.booking_care.service.QuestionService;
import com.app.booking_care.util.UrlUtil;
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
    @GetMapping()
    public List<QuestionEntity> getAll(){
        return getService().getAll();
    }

}
