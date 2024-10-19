package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.responses.StatusResponse;
import com.example.codewalker.kma.services.RankingService;
import com.example.codewalker.kma.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/ranking")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class RankingController {
    private final RankingService rankingService;
    private final StudentService studentService;
    @GetMapping("/query/update")
    public SseEmitter updateAllRankings() throws Exception {
        return rankingService.updateAllRankings();
    }
    @PostMapping("/update/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRankings() throws Exception {
        return ResponseEntity.ok(this.rankingService.updateRankings());
    }
    @PostMapping("/gpa_update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateGPA() throws Exception{
         this.rankingService.updateGPA();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                .build());
    }

    @GetMapping("/scores/{id}")
    public ResponseEntity<?> getScoresByStudentCode(@PathVariable("id") String studentCode){
        return ResponseEntity.ok(rankingService.getScoreByStudentCode(studentCode));
    }
    @PostMapping("/ranking_update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRanking(){
        this.rankingService.updateRanking();
        return ResponseEntity.ok("Update successfully!");
    }
    @PostMapping("/auto/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAllRankingsg() throws Exception {
        this.rankingService.updateAllRankings();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @PostMapping("/update/block_ranking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockRankingUpdate() throws Exception {
        this.rankingService.updateBlockRanking();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @PostMapping("/update/scholarship")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateScholarShip() throws Exception {
        this.rankingService.updateScholarShip();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @PostMapping("/update/semester_ranking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSemesterRanking() throws Exception {
        this.rankingService.updateSemesterRanking();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @PostMapping("/update/semester/table")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSemesterTable() throws Exception {
        this.rankingService.updateSemesterTable();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @PostMapping("/update/class_ranking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> classRankingUpdate() throws Exception {
        this.rankingService.updateClassRanking();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @PostMapping("/update/major_ranking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> majorRankingUpdate() throws Exception {
        this.rankingService.updateMajorRanking();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @PostMapping("/update/block_detail_ranking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockDetailRankingUpdate() throws Exception {
        this.rankingService.updateBlockDetailRanking();
        return ResponseEntity.ok(
                StatusResponse.builder()
                        .status("200")
                        .build());
    }
    @GetMapping("/school")
    public ResponseEntity<?> getRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findSchoolRanking(studentCode)
        );
    }
    @GetMapping("/top")
    public  ResponseEntity<?> findTopRanking(){
        return ResponseEntity.ok(this.rankingService.findListTopRanking());
    }
    @GetMapping("/block")
    public ResponseEntity<?> getBlockRanking(@RequestParam("student_code") String studentCode) throws Exception {
        return ResponseEntity.ok(
                this.rankingService.findBlockRanking(studentCode)
        );
    }
    @GetMapping("/class")
    public ResponseEntity<?> getClassRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findClassRanking(studentCode)
        );
    }
    @GetMapping("/major")
    public ResponseEntity<?> getMajorRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findMajorRanking(studentCode)
        );
    }
    @GetMapping("/block_details")
    public ResponseEntity<?> getBlockDetailRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findBlockDetailRanking(studentCode)
        );
    }
}
