package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.GraphRequestDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Graph;
import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.repositories.GraphRepository;
import com.example.codewalker.kma.responses.GraphFilterSubjectResponse;
import com.example.codewalker.kma.responses.SemesterSubjectsResponse;

import java.util.List;

public interface IGraphService {
    GraphFilterSubjectResponse getGraphScores(GraphRequestDTO graphRequestDTO) throws DataNotFoundException;
    Graph createScore(Graph score);
    void createListScore(List<Graph> score);
    List<SemesterSubjectsResponse> subjectResponse();
    List<GraphFilterSubjectResponse> getInfoSubjects(String yearCourse) throws DataNotFoundException;
}
