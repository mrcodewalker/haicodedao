package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hocphan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocPhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MaHocPhan", nullable = false, length = 50)
    private String maHocPhan;

    @Column(name = "TenHocPhan", nullable = false, length = 200)
    private String tenHocPhan;

    @Column(name = "DVHT", nullable = false)
    private Integer dvht;

    @Column(name = "KiHoc")
    private Integer kiHoc;

    @Column(name = "Khoa", nullable = false, length = 50)
    private String khoa;

    @Column(name = "MaBoMon", nullable = false, length = 50)
    private String maBoMon;

    @Column(name = "GhiChu", columnDefinition = "TEXT")
    private String ghiChu;
}
