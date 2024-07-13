package com.example.codewalker.services;

import com.example.codewalker.models.Semester;
import com.example.codewalker.responses.RankingResponse;

import java.util.List;

public interface ISemesterService {
//    ListScoreResponse getScoreByStudentCode(String studentCode);
    Semester createSemester(Semester semester);
    List<Semester> findAll();
    void updateRanking();
    void deleteAll();
    List<RankingResponse> getScholarship(String studentCode);
}
