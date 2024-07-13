package com.example.codewalker.services;

import com.example.codewalker.models.Score;
import com.example.codewalker.responses.ListScoreResponse;

import java.util.List;

public interface IScoreService {
    ListScoreResponse getScoreByStudentCode(String studentCode);
    Score createScore(Score score);
    List<Score> findAll();
    void updateRanking();
}
