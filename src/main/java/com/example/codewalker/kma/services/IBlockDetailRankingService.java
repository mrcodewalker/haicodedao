package com.example.codewalker.kma.services;

import com.example.codewalker.kma.responses.RankingResponse;

import java.util.List;

public interface IBlockDetailRankingService {
    void updateRanking();
    List<RankingResponse> findSchoolRanking(String studentCode);
    void updateGPA();
    RankingResponse findByRanking(Long ranking);
    List<RankingResponse> findBlockDetailRanking(String studentCode);
}
