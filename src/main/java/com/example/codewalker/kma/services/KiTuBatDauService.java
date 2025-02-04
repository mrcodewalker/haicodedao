package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.KiTuBatDau;
import com.example.codewalker.kma.repositories.KiTuBatDauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KiTuBatDauService {
    private final KiTuBatDauRepository kiTuBatDauRepository;
    public List<KiTuBatDau> getAll(){
        return this.kiTuBatDauRepository.findAll();
    }
    public double calculateBonusTotal(String courseCode, List<KiTuBatDau> allKiTuBatDau) {
        for (KiTuBatDau kiTu : allKiTuBatDau) {
            String index = kiTu.getLopViDu().toLowerCase();
            courseCode = courseCode.toLowerCase();
            if (courseCode.contains(kiTu.getLopViDu())
            || courseCode.equalsIgnoreCase(kiTu.getLopViDu())) {
                String vietTat = kiTu.getVietTat().toLowerCase();
                if ("tsat".equalsIgnoreCase(vietTat)) {
                    return 2.0;
                } else if ("chat".equalsIgnoreCase(vietTat)) {
                    return 1.5;
                }
            }
        }

        return 1.0;
    }
}
