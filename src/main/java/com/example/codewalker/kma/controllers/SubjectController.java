package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/subjects")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
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
