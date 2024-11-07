package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.GraphRequestDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Graph;
import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.repositories.GraphRepository;
import com.example.codewalker.kma.responses.GraphFilterSubjectResponse;
import com.example.codewalker.kma.responses.SemesterSubjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class GraphService implements IGraphService{
    private final SubjectService subjectService;
    private final GraphRepository graphRepository;
    @Override
    public GraphFilterSubjectResponse getGraphScores(GraphRequestDTO graphRequestDTO) throws DataNotFoundException {
        Subject subject = this.subjectService.findBySubjectName(graphRequestDTO.getSubjectName());
        if (subject==null){
            throw new DataNotFoundException("Can not find subject with subject name");
        }
        List<Graph> list = this.graphRepository.findBySubjectIdAndYear(subject.getId(), graphRequestDTO.getYearCourse());
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
        boolean valid = true;
        for (Graph clone: list) {
            double scoreRounded = Math.round(clone.getScoreFinal() / 0.2) * 0.2;

            // Kiểm tra và cập nhật giá trị trong map
            if (map.containsKey(scoreRounded)) {
                map.put(scoreRounded, map.get(scoreRounded) + 1);
            } else {
                map.put(scoreRounded, 1);
            }
            Float score = clone.getScoreFinal();
            if (score<4) lessThanFour++;
            if (score==0) equalsZero++;
            if (score<6) lessThanSix++;
            if (score<8) lessThanEight++;
            if (score<9) lessThanNine++;
            if (score>=9) greaterThanNine++;
            if (score >= 8 && score < 10) eightToTen++;
            if (score >= 7 && score < 8) sevenToEight++;
            if (score >= 5 && score < 7) fiveToSeven++;
            if (score < 5) lessThanFive++;
        }
        List<String> scores = new ArrayList<>();
        for (Double score : map.keySet()) {
            BigDecimal roundedScore = new BigDecimal(score).setScale(1, RoundingMode.HALF_UP);
            scores.add(roundedScore.toString());
        }
        List<Integer> counts = new ArrayList<>(map.values());
        long listSize = (long) list.size();
        double eightToTenPercent = ((double) eightToTen / listSize) * 100;
        double sevenToEightPercent = ((double) sevenToEight / listSize) * 100;
        double fiveToSevenPercent = ((double) fiveToSeven / listSize) * 100;
        double lessThanFivePercent = ((double) lessThanFive / listSize) * 100;

        eightToTenPercent = Math.round(eightToTenPercent * 100.0) / 100.0;
        sevenToEightPercent = Math.round(sevenToEightPercent * 100.0) / 100.0;
        fiveToSevenPercent = Math.round(fiveToSevenPercent * 100.0) / 100.0;
        lessThanFivePercent = Math.round(lessThanFivePercent * 100.0) / 100.0;

//        if ((eightToTenPercent >= 10 && eightToTenPercent <= 20)
//                && (sevenToEightPercent >= 25 && sevenToEightPercent <= 35)
//                && (fiveToSevenPercent >= 40 && fiveToSevenPercent <= 50)
//                && (lessThanFivePercent >= 5 && lessThanFivePercent <= 10)) {
//            valid = true;
//        }
        List<String> alertMessage = new ArrayList<>();
        if (eightToTenPercent>=30){
            alertMessage.add("Tỷ lệ học sinh giỏi quá cao, không phản ánh hết mức độ yêu cầu môn học!");
            valid = false;
        }
//        if (sevenToEightPercent>=45){
//            alertMessage.add("Tỷ lệ học sinh khá quá cao, không phản ánh hết mức độ yêu cầu môn học!");
//            valid = false;
//        }
//        if (fiveToSevenPercent>=60){
//            alertMessage.add("Tỷ lệ học sinh trung bình quá cao, không phản ánh hết mức độ yêu cầu môn học!");
//            valid = false;
//        }
        if (lessThanFivePercent>=20){
            alertMessage.add("Tỷ lệ học sinh yếu quá cao, không phản ánh hết mức độ yêu cầu môn học!");
            valid = false;
        }
        return GraphFilterSubjectResponse.builder()
                .subjectName(graphRequestDTO.getSubjectName())
                .score(scores)
                .count(counts.stream().map(String::valueOf).collect(Collectors.toList()))
                .total((long) list.size())
                .lessThanFour(lessThanFour)
                .equalsZero(equalsZero)
                .greaterThanNine(greaterThanNine)
                .lessThanEight(lessThanEight)
                .lessThanNine(lessThanNine)
                .lessThanSix(lessThanSix)
                .lessThanFive(lessThanFive)
                .fiveToSeven(fiveToSeven)
                .sevenToEight(sevenToEight)
                .eightToTen(eightToTen)
                .valid(valid)
                .alertMessage(alertMessage)
                .build();
    }

    @Override
    public Graph createScore(Graph score) {
        return this.graphRepository.save(score);
    }

    @Override
    @Transactional
    public void createListScore(List<Graph> score) {
        this.graphRepository.saveAll(score);
    }

    @Override
    public List<SemesterSubjectsResponse> subjectResponse() {
        List<Long> distinctSubjectIds = this.graphRepository.findDistinctSubjectIds();
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
    public List<GraphFilterSubjectResponse> getInfoSubjects(String yearCourse) throws DataNotFoundException {
        List<GraphFilterSubjectResponse> result = new ArrayList<>();
        List<Subject> subjects = this.subjectService.findAll();
        for (Subject clone : subjects){
            GraphFilterSubjectResponse response =  this.getGraphScores(GraphRequestDTO.builder()
                    .yearCourse(yearCourse)
                    .subjectName(clone.getSubjectName())
                    .build());
            if (response.getTotal()==0) continue;
            result.add(response);
        }
        result.sort((r1, r2) -> {
            if (r1.isValid() && !r2.isValid()) {
                return 1; // r1 is valid, r2 is not, so r2 comes first
            } else if (!r1.isValid() && r2.isValid()) {
                return -1; // r1 is not valid, r2 is, so r1 comes first
            }

            double eightToTenPercent1 = ((double) r1.getEightToTen() / r1.getTotal()) * 100;
            double eightToTenPercent2 = ((double) r2.getEightToTen() / r2.getTotal()) * 100;
            double lessThanFivePercent1 = ((double) r1.getLessThanFive() / r1.getTotal()) * 100;
            double lessThanFivePercent2 = ((double) r2.getLessThanFive() / r2.getTotal()) * 100;

            eightToTenPercent1 = Math.round(eightToTenPercent1 * 100.0) / 100.0;
            eightToTenPercent2 = Math.round(eightToTenPercent2 * 100.0) / 100.0;
            lessThanFivePercent1 = Math.round(lessThanFivePercent1 * 100.0) / 100.0;
            lessThanFivePercent2 = Math.round(lessThanFivePercent2 * 100.0) / 100.0;
            // Get the max of eightToTenPercent and lessThanFivePercent for each response
            double maxR1 = Math.max(eightToTenPercent1, lessThanFivePercent1);
            double maxR2 = Math.max(eightToTenPercent2, lessThanFivePercent2);

            return Double.compare(maxR2, maxR1);
        });
        return result;
    }
}
