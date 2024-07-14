package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.responses.ListScoreResponse;

import java.util.List;

public interface IScoreService {
    ListScoreResponse getScoreByStudentCode(String studentCode);
    Score createScore(Score score);
    List<Score> findAll();
    void updateRanking();
}
