package com.example.codewalker.services;

import com.example.codewalker.responses.SemesterRankingResponse;
import com.example.codewalker.responses.SubjectResponse;

import java.util.List;

public interface ISemesterRankingService {
    void updateRanking();
    void updateGPA();
    List<SemesterRankingResponse> findRanking(String studentCode);
    List<SubjectResponse> findSubjects(String studentCode);
}
