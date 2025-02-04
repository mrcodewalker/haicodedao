package com.example.codewalker.kma.repositories;


import com.example.codewalker.kma.models.KiTuBatDau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KiTuBatDauRepository extends JpaRepository<KiTuBatDau, Integer> {
    @Query("SELECT k FROM KiTuBatDau k")
    List<KiTuBatDau> getAllKiTu();
}
