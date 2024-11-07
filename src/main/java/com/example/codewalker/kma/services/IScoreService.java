package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CreateScoreDTO;
import com.example.codewalker.kma.dtos.GraphRequestDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.responses.GraphFilterSubjectResponse;
import com.example.codewalker.kma.responses.ListScoreResponse;
import com.example.codewalker.kma.responses.SemesterSubjectsResponse;

import java.util.List;

public interface IScoreService {
    ListScoreResponse getScoreByStudentCode(String studentCode);
    Score createScore(Score score);
    void createListScores(List<Score> scores);
    List<Score> findAll();
    void updateRanking();
    List<Score> createNewScore(CreateScoreDTO scoreDTO);
    GraphFilterSubjectResponse getGraphScores(GraphRequestDTO graphRequestDTO) throws DataNotFoundException;
    List<SemesterSubjectsResponse> subjectResponse();
    List<GraphFilterSubjectResponse> getInfoSubjects(String yearCourse) throws DataNotFoundException;
}
