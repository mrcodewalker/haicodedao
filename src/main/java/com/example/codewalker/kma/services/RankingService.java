package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.*;
import com.example.codewalker.kma.repositories.*;
import com.example.codewalker.kma.responses.RankingResponse;
import com.example.codewalker.kma.models.*;
import com.example.codewalker.kma.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final SubjectService subjectService;

    private List<Ranking> cachedRankings = new ArrayList<>();

    @Override
    @Transactional
    public void  updateGPA() throws Exception {
        List<Student> studentList = studentRepository.findAll();

        // Fetch all scores and subjects in one query
        List<Score> allScores = scoreRepository.findAll();
        Map<String, List<Score>> scoresByStudent = allScores.stream()
                .collect(Collectors.groupingBy(score -> score.getStudent().getStudentCode()));

        List<Subject> allSubjects = subjectService.findAll();
        Map<String, Subject> subjectMap = allSubjects.stream()
                .collect(Collectors.toMap(Subject::getSubjectName, subject -> subject));

        List<Ranking> newRankings = new ArrayList<>();

        for (Student student : studentList) {
            float gpa = 0f;
            int count = 0;

            List<Score> scoreList = scoresByStudent.get(student.getStudentCode());
            if (scoreList != null) {
                for (Score score : scoreList) {
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
        rankingRepository.saveAll(newRankings);
    }
    public void updateBlockRanking() throws Exception {
        List<Student> studentList = this.studentRepository.findAll();
        for (int i = 0; i < studentList.size(); i++) {
            List<String> convertCode = this.convertToBlockCode(studentList.get(i).getStudentCode());
            if (this.blockRankingRepository.findListFilterBlock(convertCode.get(0), convertCode.get(1), convertCode.get(2))!=null
        && this.blockRankingRepository.findListFilterBlock(convertCode.get(0), convertCode.get(1), convertCode.get(2)).size()>0){
                continue;
            }
            List<BlockRanking> blockRankings = this.findBlockRankingLocal(studentList.get(i).getStudentCode());

            if (blockRankings.size()>0){
                this.blockRankingRepository.saveAll(blockRankings);
            }
        }
    }
    public void updateClassRanking() throws Exception{
        List<Student> studentList = this.studentRepository.findAll();
        for (int i=0;i<studentList.size();i++){
            if (this.classRankingRepository.findListFilter(studentList.get(i).getStudentCode().substring(0,6).toUpperCase()) != null
                    && this.classRankingRepository.findListFilter(studentList.get(i).getStudentCode().substring(0,6).toUpperCase()).size()>0) {
                continue;
            }

            List<ClassRanking> classRankings = this.findClassRankingLocal(studentList.get(i).getStudentCode());

            if (classRankings.size() > 0) {
                this.classRankingRepository.saveAll(classRankings);
            }
        }
    }
    public void updateMajorRanking() throws Exception{
        List<Student> studentList = this.studentRepository.findAll();
        for (int i=0;i<studentList.size();i++){
            if (this.majorRankingRepository.findListFilter(studentList.get(i).getStudentCode().substring(0,2).toUpperCase()) != null
                    && this.majorRankingRepository.findListFilter(studentList.get(i).getStudentCode().substring(0,2).toUpperCase()).size()>0) {
                continue;
            }

            List<MajorRanking> majorRankings = this.findMajorRankingLocal(studentList.get(i).getStudentCode());

            if (majorRankings.size() > 0) {
                this.majorRankingRepository.saveAll(majorRankings);
            }
        }
    }
    public void updateBlockDetailRanking() throws Exception{
        List<Student> studentList = this.studentRepository.findAll();
        for (int i=0;i<studentList.size();i++){
            if (this.blockDetailRankingRepository
                    .findListFilter(studentList.get(i).getStudentCode().substring(0,4).toUpperCase()) != null
                    &&
                    this.blockDetailRankingRepository.findListFilter(studentList.get(i).getStudentCode().substring(0,4).toUpperCase()).size()>0) {
                continue;
            }

            List<BlockDetailRanking> blockDetailRankings = this.findBlockDetailRankingLocal(studentList.get(i).getStudentCode());

            if (blockDetailRankings.size() > 0) {
                this.blockDetailRankingRepository.saveAll(blockDetailRankings);
            }
        }
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
        result.add(RankingResponse.formMajorRanking(list.get(0)));
        result.add(RankingResponse.formMajorRanking(list.get(1)));
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

    public List<String> convertToBlockCode(String studentCode){
        String mainCode = studentCode.substring(0, 4);
        String cyberCode = "";
        String electronicCode ="";
        if (mainCode.equals("CT08")){
            cyberCode="AT20";
            electronicCode="DT07";
        } else {
            if (mainCode.equals("CT07")){
                cyberCode="AT19";
                electronicCode="DT06";
            } else {
                if (mainCode.equals("CT06")){
                    cyberCode="AT18";
                    electronicCode="DT05";
                } else {
                    if (mainCode.equals("CT05")){
                        cyberCode="AT17";
                        electronicCode="DT04";
                    } else {
                        if (mainCode.equals("CT04")){
                            cyberCode="AT16";
                            electronicCode="DT03";
                        } else {
                            if (mainCode.equals("CT03")){
                                cyberCode="AT15";
                                electronicCode="DT02";
                            }
                            else {
                                if (mainCode.equals("CT02")) {
                                    cyberCode = "AT14";
                                    electronicCode = "DT01";
                                }
                                else {
                                    if (mainCode.equals("CT01")) {
                                        cyberCode = "AT13";
                                        electronicCode = "AT13";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (mainCode.equals("AT20")){
            cyberCode="CT08";
            electronicCode="DT07";
        } else {
            if (mainCode.equals("AT19")){
                cyberCode="CT07";
                electronicCode="DT06";
            } else {
                if (mainCode.equals("AT18")){
                    cyberCode="CT06";
                    electronicCode="DT05";
                } else {
                    if (mainCode.equals("AT17")){
                        cyberCode="CT05";
                        electronicCode="DT04";
                    } else {
                        if (mainCode.equals("AT16")){
                            cyberCode="CT04";
                            electronicCode="DT03";
                        } else {
                            if (mainCode.equals("AT15")){
                                cyberCode="CT03";
                                electronicCode="DT02";
                            }
                            else {
                                if (mainCode.equals("AT14")) {
                                    cyberCode = "CT02";
                                    electronicCode = "DT01";
                                }
                                else {
                                    if (mainCode.equals("AT13")) {
                                        cyberCode = "CT01";
                                        electronicCode = cyberCode;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (mainCode.equals("DT07")){
            cyberCode="CT08";
            electronicCode="AT20";
        } else {
            if (mainCode.equals("DT06")){
                cyberCode="CT07";
                electronicCode="AT19";
            } else {
                if (mainCode.equals("DT05")){
                    cyberCode="CT06";
                    electronicCode="AT18";
                } else {
                    if (mainCode.equals("DT04")){
                        cyberCode="CT05";
                        electronicCode="AT17";
                    } else {
                        if (mainCode.equals("DT03")){
                            cyberCode="CT04";
                            electronicCode="AT16";
                        } else {
                            if (mainCode.equals("DT02")){
                                cyberCode="CT03";
                                electronicCode="AT15";
                            }
                            else {
                                if (mainCode.equals("DT01")) {
                                    cyberCode = "CT02";
                                    electronicCode = "AT14";
                                }
                            }
                        }
                    }
                }
            }
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
