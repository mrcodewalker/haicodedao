package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "block_detail_ranking")
public class BlockDetailRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @Column(name = "ranking")
    private Long ranking;
    @Column(name = "gpa")
    private Float gpa;
    @Column(name = "asia_gpa")
    private Float asiaGpa;
    public static BlockDetailRanking formData(Ranking ranking){
        return BlockDetailRanking.builder()
                .gpa(ranking.getGpa())
                .student(ranking.getStudent())
                .ranking(ranking.getRanking())
                .asiaGpa(ranking.getAsiaGpa())
                .build();
    }
}
