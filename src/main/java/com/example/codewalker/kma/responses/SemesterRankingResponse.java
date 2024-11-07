package com.example.codewalker.kma.responses;

import com.example.codewalker.kma.models.Scholarship;
import com.example.codewalker.kma.models.SemesterRanking;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemesterRankingResponse {
    @JsonProperty("student_name")
    private String studentName;
    @JsonProperty("student_code")
    private String studentCode;
    @JsonProperty("student_class")
    private String studentClass;
    @JsonProperty("ranking")
    private Long ranking;
    @JsonProperty("gpa")
    private Float gpa;
    @JsonProperty("asia_gpa")
    private Float asiaGpa;
    public static SemesterRankingResponse formData(SemesterRanking ranking){
        return SemesterRankingResponse.builder()
                .gpa(ranking.getGpa())
                .studentName(ranking.getStudent().getStudentName())
                .ranking(ranking.getRanking())
                .studentCode(ranking.getStudent().getStudentCode())
                .asiaGpa(ranking.getAsiaGpa())
                .studentClass(ranking.getStudent().getStudentClass())
                .build();
    }
    public static SemesterRankingResponse convert(Scholarship ranking){
        return SemesterRankingResponse.builder()
                .gpa(ranking.getGpa())
                .studentName(ranking.getStudent().getStudentName())
                .ranking(ranking.getRanking())
                .studentCode(ranking.getStudent().getStudentCode())
                .asiaGpa(ranking.getAsiaGpa())
                .studentClass(ranking.getStudent().getStudentClass())
                .build();
    }
    public static SemesterRankingResponse mappingFromObject(Object[] objects){
        Long ranking = (Long) objects[0];
        String studentCodeFromDb = (String) objects[1];
        String studentName = (String) objects[2];
        String studentClass = (String) objects[3];
        Float gpa = (Float) objects[4];
        Float asiaGpa = (Float) objects[5];

        return SemesterRankingResponse.builder()
                .ranking(ranking)
                .studentCode(studentCodeFromDb)
                .studentName(studentName)
                .studentClass(studentClass)
                .gpa(gpa)
                .asiaGpa(asiaGpa)
                .build();
    }
}
