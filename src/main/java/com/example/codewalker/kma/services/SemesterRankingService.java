package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Semester;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.repositories.StudentRepository;
import com.example.codewalker.kma.responses.RankingResponse;
import com.example.codewalker.kma.responses.SubjectResponse;
import com.example.codewalker.kma.models.*;
import com.example.codewalker.kma.repositories.*;
import com.example.codewalker.kma.models.SemesterRanking;
import com.example.codewalker.kma.repositories.SemesterRankingRepository;
import com.example.codewalker.kma.repositories.SemesterRepository;
import com.example.codewalker.kma.responses.SemesterRankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ScholarshipRepository scholarshipRepository;
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
    public void scholarshipUpdate() {
        List<Student> studentList = new ArrayList<>();
        studentList = this.studentRepository.findAll();
        for (int i=0;i<studentList.size();i++){
            if (this.scholarshipRepository.findByStudentCode(
                    studentList.get(i).getStudentCode()
            )!=null
        || this.scholarshipRepository.findListFilter(studentList.get(i).getStudentCode().substring(0,4)).size()>0){
                continue;
            }
            List<SemesterRanking> rankings = this.semesterRankingRepository
                    .findListFilter(studentList.get(i).getStudentCode().substring(0,4));
            List<Scholarship> scholarships = new ArrayList<>();
            for (int j=0;j<rankings.size();j++){
                rankings.get(j).setRanking(j+1L);
                scholarships.add(SemesterRanking.formData(rankings.get(j)));
            }
            if (scholarships.size()>0) {
                this.scholarshipRepository.saveAll(scholarships);
            }
        }
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
        responses.add(
                SemesterRankingResponse.convert(
                        this.scholarshipRepository.findByStudentCode(studentCode)
                )
        );
        List<Scholarship> scholarships = this.scholarshipRepository.findTopRankingsByStudentCodes(
                studentCode.substring(0,4)
        );
        responses.add(SemesterRankingResponse.convert(scholarships.get(1)));
        responses.add(SemesterRankingResponse.convert(scholarships.get(0)));
        responses.add(SemesterRankingResponse.convert(scholarships.get(2)));
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

    @Override
    public List<SemesterRankingResponse> getList100Students() {
        List<SemesterRankingResponse> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100, Sort.by("ranking").ascending());
        List<Scholarship> list = this.scholarshipRepository.findTop100(pageable);
        for (Scholarship clone : list){
            result.add(SemesterRankingResponse.convert(clone));
        }
        return result;
    }
}