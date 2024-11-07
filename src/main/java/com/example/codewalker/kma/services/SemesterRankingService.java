package com.example.codewalker.kma.services;

import com.example.codewalker.kma.exceptions.DataNotFoundException;
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
import lombok.Data;
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
    private final StudentFailedService studentFailedService;
    private final SubjectService subjectService;
    private List<SemesterRanking> cachedRankings;

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
    @Transactional
    public void scholarshipUpdate() {
        this.scholarshipRepository.deleteAllRecords();
        this.scholarshipRepository.resetAutoIncrement();
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
            int subjects = 0;

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
                    subjects++;
                }
            }
            if (!student.getStudentCode().contains("CT08")
                    && !student.getStudentCode().contains("CT07")
                    && !student.getStudentCode().contains("CT06")
                    && !student.getStudentCode().contains("CT05")
                    && !student.getStudentCode().contains("DT07")
                    && !student.getStudentCode().contains("DT06")
                    && !student.getStudentCode().contains("DT05")
                    && !student.getStudentCode().contains("DT04")
                    && !student.getStudentCode().contains("AT20")
                    && !student.getStudentCode().contains("AT19")
                    && !student.getStudentCode().contains("AT18")
                    && !student.getStudentCode().contains("AT17")
            ){
                continue;
            }
            if (subjects<=3){
                continue;
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
    public List<SemesterRankingResponse> findRanking(String studentCode) throws DataNotFoundException {
        studentCode = studentCode.toUpperCase();
        List<SemesterRankingResponse> responses = new ArrayList<>();
        List<Object[]> scholarships = this.semesterRankingRepository.findTopStudents(
                studentCode.substring(0, 4) + "%", studentCode
        );

        if (scholarships.size() < 3) {
            throw new DataNotFoundException("Can not be filtered right now!");
        }

        for (int count = 0; count < scholarships.size(); count++) {
            Object[] scholarship = scholarships.get(count);
            Long ranking = (Long) scholarship[0];
            String studentCodeFromDb = (String) scholarship[1];
            String studentName = (String) scholarship[2];
            String studentClass = (String) scholarship[3];
            Float gpa = (Float) scholarship[4];
            Float asiaGpa = (Float) scholarship[5];

            SemesterRankingResponse response = SemesterRankingResponse.builder()
                    .ranking(ranking)
                    .studentCode(studentCodeFromDb)
                    .studentName(studentName)
                    .studentClass(studentClass)
                    .gpa(gpa)
                    .asiaGpa(asiaGpa)
                    .build();

            responses.add(response);  // Thêm vào cuối danh sách
        }

        boolean found = false;

        // Kiểm tra xem studentCode có nằm trong responses không
        for (SemesterRankingResponse resp : responses) {
            if (resp.getStudentCode().equals(studentCode)) {
                // Di chuyển sinh viên có studentCode trùng lên đầu danh sách
                responses.add(0, responses.remove(responses.indexOf(resp))); // Di chuyển sinh viên vào đầu danh sách
                found = true;  // Đánh dấu là đã tìm thấy
                break;  // Thoát khỏi vòng lặp nếu đã tìm thấy
            }
        }

        // Nếu không tìm thấy studentCode, di chuyển phần tử cuối lên đầu
        if (!found && !responses.isEmpty()) {
            SemesterRankingResponse lastElement = responses.remove(responses.size() - 1); // Lấy phần tử cuối
            responses.add(0, lastElement); // Di chuyển phần tử cuối lên đầu danh sách
        }

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
            List<SubjectResponse> subjectResponses = this.findSubjects(clone.getStudent().getStudentCode());
            if (subjectResponses.size()<=3||clone.getGpa()<3){
                continue;
            }
            result.add(SemesterRankingResponse.convert(clone));
        }
        return result;
    }

    @Override
    public List<SemesterRankingResponse> filterListStudents(String filterCode) {
        Pageable pageable = PageRequest.of(0, 30);
        List<SemesterRanking> response = this.semesterRankingRepository.findTopWithMatchingFilterCode(filterCode, pageable);
        List<SemesterRankingResponse> result = new ArrayList<>();
        for (SemesterRanking clone : response){
            Student student = clone.getStudent();
            int count = 0;
            if (this.studentFailedService.findByStudentCode(clone.getStudent().getStudentCode()).size()>0) continue;
            List<Semester> list = this.semesterRepository.findByStudentCode(student.getStudentCode());
            for (Semester auto : list){
                if (auto.getScoreFinal()<4){
                    count++;
                    break;
                }
            }
            if(count==0){
                result.add(SemesterRankingResponse.builder()
                                .studentClass(student.getStudentClass())
                                .studentCode(student.getStudentCode())
                                .studentName(student.getStudentName())
                                .ranking(clone.getRanking())
                                .asiaGpa(clone.getAsiaGpa())
                                .gpa(clone.getGpa())
                        .build());
            }
        }
        for (int i=0;i<result.size();i++){
            result.set(i, SemesterRankingResponse.builder()
                            .gpa(result.get(i).getGpa())
                            .asiaGpa(result.get(i).getAsiaGpa())
                            .ranking(i+1L)
                            .studentName(result.get(i).getStudentName())
                            .studentClass(result.get(i).getStudentClass())
                            .studentCode(result.get(i).getStudentCode())
                    .build());
        }
        return result;
    }
    public List<SemesterRankingResponse> formData(List<SemesterRanking> semesterRankings) {
        List<SemesterRankingResponse> responses = new ArrayList<>();
        for (int i = 0; i < semesterRankings.size(); i++) {
            SemesterRanking semesterRanking = semesterRankings.get(i);
            Student student = semesterRanking.getStudent();

            responses.add(SemesterRankingResponse.builder()
                    .gpa(semesterRanking.getGpa())
                    .asiaGpa(semesterRanking.getAsiaGpa())
                    .ranking(i + 1L)  // Đặt thứ hạng từ 1
                    .studentName(student.getStudentName())
                    .studentClass(student.getStudentClass())
                    .studentCode(student.getStudentCode())
                    .build());
        }
        return responses;
    }
}