package com.example.codewalker.services;

import com.example.codewalker.responses.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassRankingService implements IClassRankingService{
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
    public List<RankingResponse> findClassRanking(String studentCode) {
        return null;
    }
}
