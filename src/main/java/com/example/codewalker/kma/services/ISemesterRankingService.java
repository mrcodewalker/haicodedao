package com.example.codewalker.kma.services;

import com.example.codewalker.kma.responses.SemesterRankingResponse;
import com.example.codewalker.kma.responses.SubjectResponse;

import java.util.List;

public interface ISemesterRankingService {
    void updateRanking();
    void updateGPA();
    List<SemesterRankingResponse> findRanking(String studentCode);
    List<SubjectResponse> findSubjects(String studentCode);
}
