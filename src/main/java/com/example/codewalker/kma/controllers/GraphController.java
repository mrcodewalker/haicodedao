package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.GraphRequestDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.services.GraphService;
import com.example.codewalker.kma.services.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/graph")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class GraphController {
    private final GraphService graphService;
    private final ScoreService scoreService;
    @PostMapping("/filter")
    public ResponseEntity<?> filterGraph(
            @RequestBody GraphRequestDTO graphRequestDTO
            ) throws DataNotFoundException{
        return ResponseEntity.ok(
                this.scoreService.getGraphScores(graphRequestDTO)
        );
    }
    @PostMapping("/filter/once")
    public ResponseEntity<?> filterGraphByOnce(
            @RequestBody GraphRequestDTO graphRequestDTO
    ) throws DataNotFoundException{
        return ResponseEntity.ok(
                this.graphService.getGraphScores(graphRequestDTO)
        );
    }
    @GetMapping("/collect/subjects")
    public ResponseEntity<?> getSubjects() throws DataNotFoundException{
        return ResponseEntity.ok(
                this.scoreService.subjectResponse()
        );
    }
    @GetMapping("/warning/subjects/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<?> warningSubjects(
            @PathVariable("id") String yearCourse
    ) throws DataNotFoundException{
        return ResponseEntity.ok(
                this.scoreService.getInfoSubjects(yearCourse)
        );
    }
    @GetMapping("/collect/subjects/once")
    public ResponseEntity<?> getSubjectsOnce() throws DataNotFoundException{
        return ResponseEntity.ok(
                this.scoreService.subjectResponse()
        );
    }
    @GetMapping("/warning/subjects/once/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<?> warningSubjectsOnce(
            @PathVariable("id") String yearCourse
    ) throws DataNotFoundException{
        return ResponseEntity.ok(
                this.scoreService.getInfoSubjects(yearCourse)
        );
    }
}
