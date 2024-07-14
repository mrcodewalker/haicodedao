package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    @GetMapping("")
    public ResponseEntity<?> getSubjectByName(@RequestParam("subject_name") String subjectName){
        return ResponseEntity.ok(subjectService.findSubjectByName(subjectName));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllSubjects(){
        return ResponseEntity.ok(subjectService.findAll());
    }
}
