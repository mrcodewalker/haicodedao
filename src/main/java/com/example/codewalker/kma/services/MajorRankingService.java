package com.example.codewalker.kma.services;

import com.example.codewalker.kma.responses.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorRankingService implements IMajorRankingService{
    @Override
    public void updateRanking() {

    }

    @Override
    public List<RankingResponse> findSchoolRanking(String studentCode) {
        return null;
    }

    @Override
    public void updateGPA() {

    }

    @Override
    public RankingResponse findByRanking(Long ranking) {
        return null;
    }

    @Override
    public List<RankingResponse> findMajorRanking(String studentCode) {
        return null;
    }
}
