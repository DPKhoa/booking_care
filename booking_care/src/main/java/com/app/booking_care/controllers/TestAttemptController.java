package com.app.booking_care.controllers;

import com.app.booking_care.entity.TestAttemptEntity;
import com.app.booking_care.model.dto.TestSubmission;
import com.app.booking_care.service.TestAttemptService;
import com.app.booking_care.util.UrlUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlUtil.TEST_ATTEMPT_URL)
public class TestAttemptController extends CommonController<TestAttemptEntity,Long, TestAttemptService>{
    public TestAttemptController(TestAttemptService service) {
        super(service);
    }
    @PostMapping("/submit")
    public ResponseEntity<String> submit(@RequestBody TestSubmission submission){
        try{
            TestAttemptEntity testAttempt = getService().saveTestAttempt(submission);
            return ResponseEntity.ok("Lưu bài làm thành công! Attempt ID: " + testAttempt.getId() + ", Total Score: " + testAttempt.getTotalScore());
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
    @GetMapping()
    public List<TestAttemptEntity> getAll(){

        return getService().getAll();
    }



}
