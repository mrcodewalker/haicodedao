package com.example.codewalker.services;

import com.example.codewalker.models.*;
import com.example.codewalker.repositories.*;
import com.example.codewalker.models.SemesterRanking;
import com.example.codewalker.repositories.SemesterRankingRepository;
import com.example.codewalker.repositories.SemesterRepository;
import com.example.codewalker.responses.SemesterRankingResponse;
import com.example.codewalker.responses.SubjectResponse;
import com.example.codewalker.models.Semester;
import com.example.codewalker.models.Student;
import com.example.codewalker.models.Subject;
import com.example.codewalker.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterRankingService implements ISemesterRankingService{
    private final SemesterRankingRepository semesterRankingRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectService subjectService;
    private List<SemesterRanking> cachedRankings;
//    @PostConstruct
//    private void init() {
//        refreshCache();
//    }
//
//    private synchronized void refreshCache() {
//        this.cachedRankings = semesterRankingRepository.findAll();
//    }

    @Override
    public void updateRanking() {
        this.cachedRankings = this.semesterRankingRepository.findAll();
        List<SemesterRanking> rankingList = this.cachedRankings;

        Collections.sort(rankingList, Comparator.comparing(SemesterRanking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            SemesterRanking semesterRanking = rankingList.get(i);
            cnt++;
            semesterRanking.setRanking(Long.parseLong(cnt+""));
        }
        this.semesterRankingRepository.saveAll(rankingList);
    }

    @Override
    @Transactional
    public void updateGPA() {
        List<Student> studentList = studentRepository.findAll();

        // Fetch all scores and subjects in one query
        List<Semester> allScores = semesterRepository.findAll();
        Map<String, List<Semester>> scoresByStudent = allScores.stream()
                .collect(Collectors.groupingBy(score -> score.getStudent().getStudentCode()));

        List<Subject> allSubjects = subjectService.findAll();
        Map<String, Subject> subjectMap = allSubjects.stream()
                .collect(Collectors.toMap(Subject::getSubjectName, subject -> subject));

        List<SemesterRanking> newRankings = new ArrayList<>();

        for (Student student : studentList) {
            float gpa = 0f;
            int count = 0;

            List<Semester> scoreList = scoresByStudent.get(student.getStudentCode());
            if (scoreList != null) {
                for (Semester score : scoreList) {
                    if (score.getSubject().getSubjectName().contains("Giáo dục thể chất")) {
                        continue;
                    }

                    Subject subject = subjectMap.get(score.getSubject().getSubjectName());
                    if (subject == null) continue;

                    float scoreValue;
                    switch (score.getScoreText()) {
                        case "A+":
                            scoreValue = 4.0f;
                            break;
                        case "A":
                            scoreValue = 3.8f;
                            break;
                        case "B+":
                            scoreValue = 3.5f;
                            break;
                        case "B":
                            scoreValue = 3.0f;
                            break;
                        case "C+":
                            scoreValue = 2.5f;
                            break;
                        case "C":
                            scoreValue = 2.0f;
                            break;
                        case "D+":
                            scoreValue = 1.5f;
                            break;
                        case "D":
                            scoreValue = 1.0f;
                            break;
                        default:
                            continue;
                    }
                    gpa += scoreValue * subject.getSubjectCredits();
                    count += subject.getSubjectCredits();
                }
            }

            if (count != 0) {
                float roundedGPA = Math.round((gpa / count) * 100) / 100f;
                float roundedAsiaGPA = Math.round((gpa / count) * 2.5f * 100) / 100f;
                newRankings.add(SemesterRanking.builder()
                        .student(student)
                        .gpa(roundedGPA)
                        .ranking(1L)
                        .asiaGpa(roundedAsiaGPA)
                        .build());
            }
        }

        newRankings.sort(Comparator.comparing(SemesterRanking::getAsiaGpa).reversed());
        for (int i = 0; i < newRankings.size(); i++) {
            newRankings.get(i).setRanking((long) (i + 1));
        }

        semesterRankingRepository.saveAll(newRankings);
    }

    @Override
    public List<SemesterRankingResponse> findRanking(String studentCode) {
        studentCode = studentCode.toUpperCase();
        List<SemesterRankingResponse> responses = new ArrayList<>();
        responses.add(new SemesterRankingResponse().formData(this.semesterRankingRepository.findByStudentId(this.studentRepository.findByStudentCode(studentCode).getStudentId())));
        responses.add(new SemesterRankingResponse().formData(this.semesterRankingRepository.findByRanking(2L)));
        responses.add(new SemesterRankingResponse().formData(this.semesterRankingRepository.findByRanking(1L)));
        responses.add(new SemesterRankingResponse().formData(this.semesterRankingRepository.findByRanking(3L)));


        return responses;
    }

    @Override
    public List<SubjectResponse> findSubjects(String studentCode) {
        List<Semester> semesters = this.semesterRepository.findByStudentCode(studentCode);
        List<SubjectResponse> list = new ArrayList<>();
        for (Semester auto : semesters){
            list.add(SubjectResponse.builder()
                            .subjectName(auto.getSubject().getSubjectName())
                            .build());
        }
        return list;
    }
}