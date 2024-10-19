package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CreateScoreDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Semester;
import com.example.codewalker.kma.repositories.ScoreRepository;
import com.example.codewalker.kma.repositories.SemesterRepository;
import com.example.codewalker.kma.repositories.StudentRepository;
import com.example.codewalker.kma.repositories.SubjectRepository;
import com.example.codewalker.kma.responses.GraphFilterSubjectResponse;
import com.example.codewalker.kma.responses.RankingResponse;
import com.example.codewalker.kma.models.*;
import com.example.codewalker.kma.responses.SemesterSubjectsResponse;
import com.example.codewalker.kma.responses.StatusResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterService implements ISemesterService{
    private final SemesterRepository semesterRepository;
    private final EntityManager entityManager;
    private final SubjectService subjectService;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ScoreRepository scoreRepository;

    @Override
    public Semester createSemester(Semester semester) {
        List<Semester> data = semesterRepository.findByStudentCode(semester.getStudent().getStudentCode());
        for (Semester entry : data){
            if (entry.getSubject().getSubjectName().equals(semester.getSubject().getSubjectName())
                    || Normalizer.normalize(entry.getSubject().getSubjectName(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").equalsIgnoreCase(
                    Normalizer.normalize(semester.getSubject().getSubjectName(), Normalizer.Form.NFD).replaceAll("\\p{M}", ""))){
                semester.setId(entry.getId());
                semester.setScoreFirst(semester.getScoreFirst());
                semester.setScoreOverall(semester.getScoreOverall());
                semester.setScoreFinal(semester.getScoreFinal());
                semester.setScoreText(semester.getScoreText());
                semester.setScoreSecond(semester.getScoreSecond());
                return semesterRepository.save(semester);
            }
        }
        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> findAll() {
        return null;
    }

    @Override
    public void updateRanking() {

    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Semester").executeUpdate();
        entityManager.createQuery("ALTER TABLE Semester AUTO_INCREMENT = 1").executeUpdate();
    }

    @Override
    public List<RankingResponse> getScholarship(String studentCode) {
        return null;
    }

    @Override
    public List<Semester> createNewScore(CreateScoreDTO scoreDTO) {
        List<Semester> scores = new ArrayList<>();
        for (String index: scoreDTO.getListStudent()){
            Student student = this.studentRepository.findByStudentCode(index.toUpperCase());
            scores.add(
                    Semester.builder()
                            .subject(this.subjectRepository.findFirstBySubjectName(scoreDTO.getSubjectName()))
                            .student(student)
                            .scoreFirst(scoreDTO.getScoreFirst())
                            .scoreSecond(scoreDTO.getScoreSecond())
                            .scoreText(scoreDTO.getScoreText())
                            .scoreOverall(scoreDTO.getScoreOverall())
                            .scoreFinal(scoreDTO.getScoreFinal())
                            .build()
            );
        }
//        System.out.println(scores);
        return this.semesterRepository.saveAll(scores);
    }

    @Override
    public GraphFilterSubjectResponse getGraphScores(String subjectName) throws DataNotFoundException {
        Subject subject = this.subjectService.findBySubjectName(subjectName);
        if (subject==null){
            throw new DataNotFoundException("Can not find subject with subject name");
        }
        List<Semester> list = this.semesterRepository.findBySubjectId(subject.getId());
        Map<Double, Integer> map = new TreeMap<>();
        long equalsZero = 0L;
        long lessThanFour = 0L;
        long lessThanSix = 0L;
        long lessThanEight = 0L;
        long lessThanNine = 0L;
        long greaterThanNine = 0L;
        long eightToTen = 0L;
        long sevenToEight = 0L;
        long fiveToSeven = 0L;
        long lessThanFive = 0L;
        for (Semester semester: list) {
            double scoreRounded = Math.round(semester.getScoreFinal() / 0.2) * 0.2;

            // Kiểm tra và cập nhật giá trị trong map
            if (map.containsKey(scoreRounded)) {
                map.put(scoreRounded, map.get(scoreRounded) + 1);
            } else {
                map.put(scoreRounded, 1);
            }
            Float score = semester.getScoreFinal();
            if (score<4) lessThanFour++;
            if (score==0) equalsZero++;
            if (score<6) lessThanSix++;
            if (score<8) lessThanEight++;
            if (score<9) lessThanNine++;
            if (score>=9) greaterThanNine++;
            if (score>=8&&score<10){
                eightToTen++;
            }
            if (score>=7&&score<8){
                sevenToEight++;
            }
            if (score>=5&&score<7){
                fiveToSeven++;
            }
            if (score<5){
                lessThanFive++;
            }
        }
        List<String> scores = new ArrayList<>();
        for (Double score : map.keySet()) {
            BigDecimal roundedScore = new BigDecimal(score).setScale(1, RoundingMode.HALF_UP);
            scores.add(roundedScore.toString());
        }
        List<Integer> counts = new ArrayList<>(map.values());
        return GraphFilterSubjectResponse.builder()
                .score(scores)
                .count(counts.stream().map(String::valueOf).collect(Collectors.toList()))
                .total((long) list.size())
                .lessThanFour(lessThanFour)
                .equalsZero(equalsZero)
                .greaterThanNine(greaterThanNine)
                .lessThanEight(lessThanEight)
                .lessThanNine(lessThanNine)
                .lessThanSix(lessThanSix)
                .eightToTen(eightToTen)
                .sevenToEight(sevenToEight)
                .fiveToSeven(fiveToSeven)
                .lessThanFive(lessThanFive)
                .build();
    }

    @Override
    public List<SemesterSubjectsResponse> subjectResponse() {
        List<Long> distinctSubjectIds = semesterRepository.findDistinctSubjectIds();
        List<SemesterSubjectsResponse> responses = new ArrayList<>();
        for (Long clone: distinctSubjectIds){
            Subject found = this.subjectService.findBySubjectId(clone);
            responses.add(
                    SemesterSubjectsResponse.builder()
                            .subjectCredits(found.getSubjectCredits())
                            .subjectName(found.getSubjectName())
                            .build());

        }
        return responses;
    }

    @Override
    @Transactional
    public StatusResponse resetSemesterTable() {
        this.semesterRepository.deleteAllScores();
        this.semesterRepository.resetAutoIncrement();
        return StatusResponse.builder()
                .status("200")
                .build();
    }

    @Override
    @Transactional
    public StatusResponse parsingData() {
        StatusResponse waiting = this.resetSemesterTable();
        List<Score> list = this.scoreRepository.findScoresWithLatestSemester();
        List<Semester> result = list.stream().map(
                scores -> {
                    return Semester.builder()
                            .student(scores.getStudent())
                            .subject(scores.getSubject())
                            .scoreText(scores.getScoreText())
                            .scoreSecond(scores.getScoreSecond())
                            .scoreFinal(scores.getScoreFinal())
                            .scoreFirst(scores.getScoreFirst())
                            .scoreOverall(scores.getScoreOverall())
                            .build();
                }
        ).collect(Collectors.toList());
        this.semesterRepository.saveAll(result);
        return StatusResponse.builder()
                .status("200")
                .build();
    }

}
