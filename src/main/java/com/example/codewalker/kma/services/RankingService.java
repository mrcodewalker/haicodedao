package com.example.codewalker.kma.services;

import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.*;
import com.example.codewalker.kma.repositories.*;
import com.example.codewalker.kma.responses.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService implements IRankingService {
    private final RankingRepository rankingRepository;
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final MajorRankingRepository majorRankingRepository;
    private final BlockRankingRepository blockRankingRepository;
    private final BlockDetailRankingRepository blockDetailRankingRepository;
    private final ClassRankingRepository classRankingRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final SemesterRankingRepository semesterRankingRepository;
    private final SubjectService subjectService;
    private final SemesterRepository semesterRepository;
    private final StudentFailedRepository studentFailedRepository;
    private List<Ranking> cachedRankings = new ArrayList<>();
    public Long setTimeOut = 2*1000L;

    @Override
    public void  updateGPA() throws Exception {
        ScoreService.cache.clear();
        List<Student> studentList = studentRepository.findAll();
        // Fetch all scores and subjects in one query

        List<Subject> allSubjects = subjectService.findAll();
        Map<String, Subject> subjectMap = allSubjects.stream()
                .collect(Collectors.toMap(Subject::getSubjectName, subject -> subject));

        List<Ranking> newRankings = new ArrayList<>();

        for (Student student : studentList) {
            float gpa = 0f;
            int count = 0;
            int subjects = 0;

            List<Score> scoreList = this.scoreRepository.findByStudentCode(student.getStudentCode());
            if (scoreList.size()>0) {
                for (Score score : scoreList) {
                    if (score.getSubject().getSubjectName().contains("Giáo dục thể chất")) {
                        subjects++;
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
                            scoreValue = 2.4f;
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
                        case "F":
                            scoreValue = 0.0f;
                            break;
                        default:
                            continue;
                    }
                    gpa += scoreValue * subject.getSubjectCredits();
                    count += subject.getSubjectCredits();
                    subjects++;
                }
            }
            String studentCode = student.getStudentCode();
            if (!studentCode.contains("CT08")
                    && !studentCode.contains("CT07")
                    && !studentCode.contains("CT06")
                    && !studentCode.contains("CT05")
                    && !studentCode.contains("DT07")
                    && !studentCode.contains("DT06")
                    && !studentCode.contains("DT05")
                    && !studentCode.contains("DT04")
                    && !studentCode.contains("AT20")
                    && !studentCode.contains("AT19")
                    && !studentCode.contains("AT18")
                    && !studentCode.contains("AT17")
            ){
                continue;
            }
            if (subjects<=3){
                continue;
            }
            if (count != 0) {
                float roundedGPA = Math.round((gpa / count) * 100) / 100f;
                float roundedAsiaGPA = Math.round((gpa / count) * 2.5f * 100) / 100f;
                newRankings.add(Ranking.builder()
                        .student(student)
                        .gpa(roundedGPA)
                        .ranking(1L)
                        .asiaGpa(roundedAsiaGPA)
                        .build());
            }
        }

        newRankings.sort(Comparator.comparing(Ranking::getGpa).reversed());
        for (int i = 0; i < newRankings.size(); i++) {
            newRankings.get(i).setRanking((long) (i + 1));
        }
        this.rankingRepository.deleteAllRecords();
        this.rankingRepository.resetAutoIncrement();
        rankingRepository.saveAll(newRankings);
    }
    @Override
    @Transactional
    public SseEmitter updateAllRankings() throws Exception {
        SseEmitter emitter = new SseEmitter(6000000L);

        new Thread(() -> {
            try {
                // Bắt đầu cập nhật GPA và các thứ khác
                emitter.send("Updating GPA...");
                Thread.sleep(this.setTimeOut);
                updateGPA();
                emitter.send("GPA has been updated");
                Thread.sleep(this.setTimeOut);

                emitter.send("Updating Block Ranking...");
                Thread.sleep(this.setTimeOut);
                updateBlockRanking();
                emitter.send("Block Ranking has been updated");
                Thread.sleep(this.setTimeOut);


                emitter.send("Updating Class Ranking...");
                Thread.sleep(this.setTimeOut);
                updateClassRanking();
                emitter.send("Class Ranking has been updated");
                Thread.sleep(this.setTimeOut);

                emitter.send("Updating Major Ranking...");
                Thread.sleep(this.setTimeOut);
                updateMajorRanking();
                emitter.send("Major Ranking has been updated");
                Thread.sleep(this.setTimeOut);

                emitter.send("Updating Block Detail Ranking...");
                Thread.sleep(this.setTimeOut);
                updateBlockDetailRanking();
                emitter.send("Block Detail Ranking has been updated");
                Thread.sleep(this.setTimeOut);

                emitter.send("Updating Semester Table...");
                Thread.sleep(this.setTimeOut);
                updateSemesterTable();
                emitter.send("Semester Table has been updated");
                Thread.sleep(this.setTimeOut);

                emitter.send("Updating Semester Ranking...");
                Thread.sleep(this.setTimeOut);
                updateSemesterRanking();
                emitter.send("Semester Ranking has been updated");
                Thread.sleep(this.setTimeOut);

                emitter.send("Updating Scholarship List...");
                Thread.sleep(this.setTimeOut);
                updateScholarShip();
                emitter.send("Scholarship List has been updated");
                Thread.sleep(this.setTimeOut);

                emitter.send("Updating GPA (FINAL)...");
                Thread.sleep(this.setTimeOut);
                updateGPA();

                emitter.send("All updates completed");
                Thread.sleep(this.setTimeOut);
            } catch (Exception e) {
                emitter.completeWithError(e); // Hoàn tất với lỗi
            } finally {
                emitter.complete(); // Đóng emitter
            }
        }).start();

        return emitter; // Trả về emitter
    }
    @Override
    @Transactional
    public StatusResponse updateRankings() throws Exception {
        updateBlockRanking();
        updateClassRanking();
        updateBlockDetailRanking();
        updateMajorRanking();
        updateSemesterTable();
        updateSemesterRanking();
        updateScholarShip();
        return StatusResponse.builder()
                .status("200")
                .build();
    }


    public void updateBlockRanking() throws Exception {
        List<Student> studentList = this.studentRepository.findAll();
        this.blockRankingRepository.deleteAllRecords();
        this.blockRankingRepository.resetAutoIncrement();
        List<BlockRanking> result = new ArrayList<>();
        String technology[] = new String[]{
                "CT08%", "CT07%", "CT06%", "CT05%", "CT04%",
                "CT03%"
        };
        String cyber[] = new String[]{
                "AT20%", "AT19%", "AT18%", "AT17%", "AT16%",
                "AT15%"
        };
        String electric[] = new String[]{
                "DT07%", "DT06%", "DT05%", "DT04%", "DT03%",
                "DT02%"
        };
        for (int i=0;i<technology.length;i++){
            long count = 0;
            List<Ranking> list = this.rankingRepository.findBlockRanking(technology[i], cyber[i], electric[i]);
            for (Ranking clone: list){
                result.add(
                        BlockRanking.builder()
                                .student(clone.getStudent())
                                .asiaGpa(clone.getAsiaGpa())
                                .gpa(clone.getGpa())
                                .ranking(++count)
                                .build()
                );
            }
        }
        this.blockRankingRepository.saveAll(result);
    }
    public void updateSemesterRanking() {
        this.semesterRankingRepository.deleteAllRecords();
        this.semesterRankingRepository.resetAutoIncrement();
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
            System.out.println(student);
            float gpa = 0f;
            int count = 0;
            int subjects = 0;

            List<Semester> scoreList = scoresByStudent.get(student.getStudentCode());
            if (scoreList != null) {
                System.out.println(scoreList);
                for (Semester score : scoreList) {
                    if (score.getSubject().getSubjectName().contains("Giáo dục thể chất")) {
                        subjects++;
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
                        case "F":
                            scoreValue = 0.0f;
                            break;
                        default:
                            continue;
                    }
                    gpa += scoreValue * subject.getSubjectCredits();
                    count += subject.getSubjectCredits();
                    subjects++;
                }
            }
            String studentCode = student.getStudentCode();
            if (!studentCode.contains("CT08")
                    && !studentCode.contains("CT07")
                    && !studentCode.contains("CT06")
                    && !studentCode.contains("CT05")
                    && !studentCode.contains("DT07")
                    && !studentCode.contains("DT06")
                    && !studentCode.contains("DT05")
                    && !studentCode.contains("DT04")
                    && !studentCode.contains("AT20")
                    && !studentCode.contains("AT19")
                    && !studentCode.contains("AT18")
                    && !studentCode.contains("AT17")
            ){
                continue;
            }
            if (subjects<=3){
                continue;
            }
            if (count != 0 && gpa != 0) {
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
            newRankings.get(i).setId((long) i+1);
            newRankings.get(i).setRanking((long) (i + 1));
        }
        semesterRankingRepository.saveAll(newRankings);
    }
    public void updateClassRanking() throws Exception{
        List<String> studentList = this.studentRepository.findDistinctClass();
        this.classRankingRepository.deleteAllRecords();
        this.classRankingRepository.resetAutoIncrement();
        List<ClassRanking> result = new ArrayList<>();
        for (int i=0;i<studentList.size();i++){
            long count = 0;
            List<Ranking> classRankings = this.rankingRepository.findOneParam(studentList.get(i)+"%");
            for (Ranking ranking: classRankings){
                result.add(
                        ClassRanking.builder()
                                .gpa(ranking.getGpa())
                                .ranking(++count)
                                .asiaGpa(ranking.getAsiaGpa())
                                .student(ranking.getStudent())
                                .build()
                );
            }
        }
        this.classRankingRepository.saveAll(result);
    }
    public void updateBlockDetailRanking() throws Exception{
        this.blockDetailRankingRepository.deleteAllRecords();
        this.blockDetailRankingRepository.resetAutoIncrement();
        List<String> studentList = this.studentRepository.findDistinctBlockDetail();
        List<BlockDetailRanking> result = new ArrayList<>();
        for (int i=0;i<studentList.size();i++){
            long count = 0;
            List<Ranking> blockDetailRankings = this.rankingRepository.findOneParam(studentList.get(i)+"%");

            for(Ranking clone: blockDetailRankings){
                result.add(
                        BlockDetailRanking.builder()
                                .ranking(++count)
                                .asiaGpa(clone.getAsiaGpa())
                                .gpa(clone.getGpa())
                                .student(clone.getStudent())
                                .build()
                );
            }
        }
        this.blockDetailRankingRepository.saveAll(result);
    }
    public void updateScholarShip() {
        this.scholarshipRepository.deleteAllRecords();
        this.scholarshipRepository.resetAutoIncrement();
        String technology[] = new String[]{
                "CT08%", "CT07%", "CT06%", "CT05%", "CT04%",
                "CT03%"
        };
        String cyber[] = new String[]{
                "AT20%", "AT19%", "AT18%", "AT17%", "AT16%",
                "AT15%"
        };
        String electric[] = new String[]{
                "DT07%", "DT06%", "DT05%", "DT04%", "DT03%",
                "DT02%"
        };
        List<Scholarship> result = new ArrayList<>();
        for (int i=0;i<technology.length;i++){
            long count = 0;
            List<SemesterRanking> list = this.semesterRankingRepository.findBlockRanking(
                    technology[i], cyber[i], electric[i]
            );
            for (SemesterRanking clone: list){
                result.add(
                        Scholarship.builder()
                                .ranking(++count)
                                .asiaGpa(clone.getAsiaGpa())
                                .gpa(clone.getGpa())
                                .student(clone.getStudent())
                                .build()
                );
            }
        }
        this.scholarshipRepository.saveAll(result);
    }
    public void updateMajorRanking() throws Exception{
        List<String> studentList = this.studentRepository.findDistinctMajor();
        this.majorRankingRepository.deleteAllRecords();
        this.majorRankingRepository.resetAutoIncrement();
        List<MajorRanking> result = new ArrayList<>();
        for (int i=0;i<studentList.size();i++){
            long count = 0;
            List<Ranking> majorRankings = this.rankingRepository.findOneParam(
                    studentList.get(i)+"%"
            );

            for (Ranking clone: majorRankings){
                result.add(
                  MajorRanking.builder()
                          .ranking(++count)
                          .asiaGpa(clone.getAsiaGpa())
                          .gpa(clone.getGpa())
                          .student(clone.getStudent())
                          .build()
                );
            }
        }
        this.majorRankingRepository.saveAll(result);
    }
    public void updateSemesterTable() {
        this.semesterRepository.deleteAllScores();
        this.semesterRepository.resetAutoIncrement();
        this.studentFailedRepository.deleteAllScores();
        this.studentFailedRepository.resetAutoIncrement();
        List<Score> list = this.scoreRepository.findScoresWithLatestSemester();
        List<Semester> result = list.stream().map(
                scores -> {
                    if (scores.getScoreFinal()<4){
                        this.studentFailedRepository.save(StudentFailed.builder()
                                        .studentCode(scores.getStudent().getStudentCode())
                                .build());
                    }
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
    }
    public List<BlockRanking> findBlockRankingLocal(String studentCode) throws Exception {
        List<String> convertCode = this.convertToBlockCode(studentCode);
        String mainCode = convertCode.get(0);
        String cyberCode = convertCode.get(1);
        String electronicCode = convertCode.get(2);
        List<BlockRanking> blockRankings = new ArrayList<>();
        List<Ranking> rankings = rankingRepository.findListFilterBlock(mainCode, cyberCode, electronicCode);
        if (this.blockRankingRepository.findListFilterBlock(mainCode, cyberCode, electronicCode)==null
                || this.blockRankingRepository.findListFilterBlock(mainCode, cyberCode, electronicCode).size()==0) {
            for (int i = 0; i < rankings.size(); i++) {
                rankings.get(i).setRanking(i + 1L);
                blockRankings.add(BlockRanking.formData(rankings.get(i)));
            }
        }
        return blockRankings;
    }
    public List<ClassRanking> findClassRankingLocal(String studentCode) {
        String classCode = studentCode.substring(0, 6).toUpperCase();
        List<Ranking> list = this.rankingRepository.findListFilter(classCode);
        List<ClassRanking> result = new ArrayList<>();
        if (this.classRankingRepository.findListFilter(classCode)==null
    || this.classRankingRepository.findListFilter(classCode).size()==0 ) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setRanking(i + 1L);
                ClassRanking classRanking = ClassRanking.builder()
                        .student(list.get(i).getStudent())
                        .ranking(list.get(i).getRanking())
                        .asiaGpa(list.get(i).getAsiaGpa())
                        .gpa(list.get(i).getGpa())
                        .build();
                result.add(classRanking);
            }
        }
        return result;
    }
    public List<MajorRanking> findMajorRankingLocal(String studentCode) {
        String majorCode = studentCode.substring(0, 2);
        List<Ranking> list = this.rankingRepository.findListFilter(majorCode);
        List<MajorRanking> majorRankings = new ArrayList<>();
        if (majorRankingRepository.findListFilter(majorCode)==null
        || majorRankingRepository.findListFilter(majorCode).size()==0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setRanking(i + 1L);
                majorRankings.add(MajorRanking.formData(list.get(i)));
            }
        }
        return majorRankings;
    }
    public List<BlockDetailRanking> findBlockDetailRankingLocal(String studentCode) {
        String mainCode = studentCode.substring(0, 4);
        List<Ranking> list = this.rankingRepository.findListFilter(mainCode);
        List<BlockDetailRanking> blockDetailRankings = new ArrayList<>();
        if (this.blockDetailRankingRepository.findListFilter(mainCode)==null
        || this.blockDetailRankingRepository.findListFilter(mainCode).size()==0 ) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setRanking(i + 1L);
                blockDetailRankings.add(BlockDetailRanking.formData(list.get(i)));
            }
        }

        return blockDetailRankings;
    }
    @Override
    public RankingResponse findByRanking(Long ranking) {
        Ranking rankingEntity = rankingRepository.findByRanking(ranking);
        return RankingResponse.formData(rankingEntity);
    }

    private List<RankingResponse> findRankingResponses(List<Ranking> rankingList, String studentCode) {
        List<RankingResponse> responses = new ArrayList<>();
        Optional<Ranking> optionalRanking = rankingList.stream()
                .filter(ranking -> ranking.getStudent().getStudentCode().equalsIgnoreCase(studentCode))
                .findFirst();
        optionalRanking.ifPresent(ranking -> responses.add(new RankingResponse().formData(ranking)));
        responses.add(new RankingResponse().formData(rankingList.get(1)));
        responses.add(new RankingResponse().formData(rankingList.get(0)));
        responses.add(new RankingResponse().formData(rankingList.get(2)));
        return responses;
    }

    @Override
    public List<RankingResponse> findBlockRanking(String studentCode) throws Exception {
        List<RankingResponse> result = new ArrayList<>();
        List<String> convertCode = this.convertToBlockCode(studentCode);
        result.add(RankingResponse.formBlockRanking(blockRankingRepository.findByStudentCode(studentCode)));
        List<BlockRanking> list = this.blockRankingRepository
                .findTopRankingsByStudentCodes(convertCode.get(0), convertCode.get(1), convertCode.get(2));
        result.add(RankingResponse.formBlockRanking(list.get(1)));
        result.add(RankingResponse.formBlockRanking(list.get(0)));
        result.add(RankingResponse.formBlockRanking(list.get(2)));

        return result;
    }

    @Override
    public List<RankingResponse> findClassRanking(String studentCode) {
        String classCode = studentCode.substring(0, 6).toUpperCase();

        List<RankingResponse> result = new ArrayList<>();
        result.add(RankingResponse.formClassRanking(classRankingRepository.findByStudentCode(studentCode)));
        List<ClassRanking> list = this.classRankingRepository
                .findTopRankingsByStudentCodes(classCode);
        result.add(RankingResponse.formClassRanking(list.get(1)));
        result.add(RankingResponse.formClassRanking(list.get(0)));
        result.add(RankingResponse.formClassRanking(list.get(2)));
        return result;
    }

    @Override
    public void updateRanking() {
        // Implement logic if needed
    }

    @Override
    public List<RankingResponse> findSchoolRanking(String studentCode) {
        List<RankingResponse> result = new ArrayList<>();
        result.add(RankingResponse.formData(rankingRepository.findByStudentCode(studentCode)));
        List<Ranking> list = this.rankingRepository
                .findTopRankingsByStudentCodes(studentCode);
        result.add(RankingResponse.formData(list.get(1)));
        result.add(RankingResponse.formData(list.get(0)));
        result.add(RankingResponse.formData(list.get(2)));
        return result;
    }

    @Override
    public List<RankingResponse> findMajorRanking(String studentCode) {
        String majorCode = studentCode.substring(0, 2);
        List<RankingResponse> result = new ArrayList<>();
        result.add(RankingResponse.formMajorRanking(majorRankingRepository.findByStudentCode(studentCode)));
        List<MajorRanking> list = this.majorRankingRepository
                .findTopRankingsByStudentCodes(majorCode);
        result.add(RankingResponse.formMajorRanking(list.get(1)));
        result.add(RankingResponse.formMajorRanking(list.get(0)));
        result.add(RankingResponse.formMajorRanking(list.get(2)));
        return result;
    }

    @Override
    public List<RankingResponse> findBlockDetailRanking(String studentCode) {
        String mainCode = studentCode.substring(0, 4);

        List<RankingResponse> result = new ArrayList<>();
        result.add(RankingResponse.formBlockDetailRanking(blockDetailRankingRepository.findByStudentCode(studentCode)));
        List<BlockDetailRanking> list = this.blockDetailRankingRepository
                .findTopRankingsByStudentCodes(mainCode);
        result.add(RankingResponse.formBlockDetailRanking(list.get(1)));
        result.add(RankingResponse.formBlockDetailRanking(list.get(0)));
        result.add(RankingResponse.formBlockDetailRanking(list.get(2)));
        return result;
    }

    @Override
    public List<RankingResponse> findListTopRanking() {
        List<Ranking> list = this.rankingRepository
                .findListTopRanking();
        List<RankingResponse> result = new ArrayList<>();
        result.add(RankingResponse.formData(list.get(1)));
        result.add(RankingResponse.formData(list.get(0)));
        result.add(RankingResponse.formData(list.get(2)));
        return result;
    }

    @Override
    public ListScoreResponse getScoreByStudentCode(String studentCode) {
        Student student = this.findByStudentCode(studentCode);
        List<Score> list = scoreRepository.findByStudentId(student.getStudentId());
        List<ScoreResponse> data = new ArrayList<>();
        for (Score clone : list){
            ScoreResponse scoreResponse = ScoreResponse.builder()
                    .scoreFinal(clone.getScoreFinal())
                    .scoreSecond(clone.getScoreSecond())
                    .scoreOverall(clone.getScoreOverall())
                    .scoreFirst(clone.getScoreFirst())
                    .scoreText(clone.getScoreText())
                    .subjectName(clone.getSubject().getSubjectName())
                    .subjectCredit(clone.getSubject().getSubjectCredits())
                    .build();
            data.add(scoreResponse);
        }
        return ListScoreResponse.builder()
                .studentResponse(StudentResponse.builder()
                        .studentClass(student.getStudentClass())
                        .studentName(student.getStudentName())
                        .studentCode(student.getStudentCode())
                        .build())
                .scoreResponses(data)
                .build();
    }

    public Student findByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }
    public List<String> convertToBlockCode(String studentCode){
        String mainCode = studentCode.substring(0, 4);
        String cyberCode = "";
        String electronicCode ="";
        Map<String, String[]> codeMap = new HashMap<>();
        codeMap.put("CT08", new String[]{"AT20", "DT07"});
        codeMap.put("CT07", new String[]{"AT19", "DT06"});
        codeMap.put("CT06", new String[]{"AT18", "DT05"});
        codeMap.put("CT05", new String[]{"AT17", "DT04"});
        codeMap.put("CT04", new String[]{"AT16", "DT03"});
        codeMap.put("CT03", new String[]{"AT15", "DT02"});
        codeMap.put("CT02", new String[]{"AT14", "DT01"});
        codeMap.put("CT01", new String[]{"AT13", "AT13"});

        codeMap.put("AT20", new String[]{"CT08", "DT07"});
        codeMap.put("AT19", new String[]{"CT07", "DT06"});
        codeMap.put("AT18", new String[]{"CT06", "DT05"});
        codeMap.put("AT17", new String[]{"CT05", "DT04"});
        codeMap.put("AT16", new String[]{"CT04", "DT03"});
        codeMap.put("AT15", new String[]{"CT03", "DT02"});
        codeMap.put("AT14", new String[]{"CT02", "DT01"});
        codeMap.put("AT13", new String[]{"CT01", "CT01"});

        codeMap.put("DT07", new String[]{"CT08", "AT20"});
        codeMap.put("DT06", new String[]{"CT07", "AT19"});
        codeMap.put("DT05", new String[]{"CT06", "AT18"});
        codeMap.put("DT04", new String[]{"CT05", "AT17"});
        codeMap.put("DT03", new String[]{"CT04", "AT16"});
        codeMap.put("DT02", new String[]{"CT03", "AT15"});
        codeMap.put("DT01", new String[]{"CT02", "AT14"});

        if (codeMap.containsKey(mainCode)) {
            String[] codes = codeMap.get(mainCode);
            cyberCode = codes[0];
            electronicCode = codes[1];
        }
        if (cyberCode.equals("")){
            cyberCode = mainCode;
            electronicCode = mainCode;
        }
        List<String> result = new ArrayList<>();
        result.add(mainCode);
        result.add(cyberCode);
        result.add(electronicCode);
        return result;
    }
}
