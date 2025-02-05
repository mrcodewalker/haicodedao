package com.example.codewalker.kma.services;

import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.responses.RankingResponse;
import com.example.codewalker.kma.responses.SemesterRankingResponse;
import com.example.codewalker.kma.responses.SubjectResponse;

import java.util.List;

public interface ISemesterRankingService {
    void updateRanking();
    void updateGPA();
    List<SemesterRankingResponse> findRanking(String studentCode) throws DataNotFoundException;
    List<SubjectResponse> findSubjects(String studentCode);
    List<SemesterRankingResponse> getList100Students();
    List<SemesterRankingResponse> filterListStudents(String filterCode);
}
