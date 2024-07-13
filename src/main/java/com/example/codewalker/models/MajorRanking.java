package com.example.codewalker.models;

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
@Table(name = "major_ranking")
public class MajorRanking {
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
    public static MajorRanking formData(Ranking ranking){
        return MajorRanking.builder()
                .gpa(ranking.getGpa())
                .student(ranking.getStudent())
                .ranking(ranking.getRanking())
                .asiaGpa(ranking.getAsiaGpa())
                .build();
    }
}
