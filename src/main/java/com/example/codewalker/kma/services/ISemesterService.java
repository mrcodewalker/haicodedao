package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CreateScoreDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Semester;
import com.example.codewalker.kma.responses.*;

import java.util.List;

public interface ISemesterService {
//    ListScoreResponse getScoreByStudentCode(String studentCode);
    Semester createSemester(Semester semester);
    List<Semester> findAll();
    void updateRanking();
    void deleteAll();
    List<RankingResponse> getScholarship(String studentCode);
    List<Semester> createNewScore(CreateScoreDTO scoreDTO);
    GraphFilterSubjectResponse getGraphScores(String subjectName) throws DataNotFoundException;
    List<SemesterSubjectsResponse> subjectResponse();
    StatusResponse resetSemesterTable();
    StatusResponse parsingData();
}
