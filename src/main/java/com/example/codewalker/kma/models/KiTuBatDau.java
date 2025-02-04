package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kitubatdau")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KiTuBatDau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LopViDu", nullable = false)
    private String lopViDu;

    @Column(name = "VietTat", nullable = false, length = 50)
    private String vietTat;

    @Column(name = "HeDaoTao")
    private String heDaoTao;

    @Column(name = "LoaiDaoTao")
    private String loaiDaoTao;

    @Column(name = "giaTriSoSanh")
    private String giaTriSoSanh;
}

