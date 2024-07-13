package com.example.codewalker.controllers;

import com.example.codewalker.models.Schedule;
import com.example.codewalker.services.ScheduleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/schedules")
@CrossOrigin(origins = "http://localhost:4200")
public class ScheduleController {
    private final ScheduleService scheduleService;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadScheduleFile(@RequestParam("path") String pathName){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Path path = Paths.get(pathName);

            if (!Files.exists(path)) {
                return ResponseEntity.badRequest().body("File does not exist.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

            JsonNode[] jsonNodes = objectMapper.readValue(path.toFile(), JsonNode[].class);

            for (JsonNode jsonNode : jsonNodes) {
                String startDayString = jsonNode.get("start_day").asText();
                String endDayString = jsonNode.get("end_day").asText();

                LocalDate startDay = LocalDate.parse(startDayString, formatter);
                LocalDate endDay = LocalDate.parse(endDayString, formatter);

                ((ObjectNode) jsonNode).put("start_day", startDay.toString());
                ((ObjectNode) jsonNode).put("end_day", endDay.toString());
            }

            Schedule[] schedulesArray = objectMapper.treeToValue(objectMapper.valueToTree(jsonNodes), Schedule[].class);
            List<Schedule> schedules = Arrays.asList(schedulesArray);

            for (Schedule schedule : schedules){
                this.scheduleService.createSchedule(schedule);
            }
            return ResponseEntity.ok("Updated database successfully!");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to upload and process the file.");
        }
    }
    @GetMapping("/schedule")
    public ResponseEntity<?> getSubjectSchedule(@RequestParam("student_course") String studentCourse){
        return ResponseEntity.ok(this.scheduleService.findByStudentCourse(studentCourse));
    }
    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects(@RequestParam("student_course") String studentCourse){
        return ResponseEntity.ok(this.scheduleService.findAllSubjects(studentCourse));
    }
    @GetMapping("/detail/subject")
    public ResponseEntity<?> getDetailSubject(@RequestParam("subject_name") String subjectName){
        return ResponseEntity.ok(this.scheduleService.findScheduleTime(subjectName));
    }
    @PostMapping("/wanna/{student_course}")
    public ResponseEntity<?> wannaSubjects(
            @RequestParam("day_in_week") List<String> dayInWeek,
            @PathVariable("student_course") String studentCourse){
        return ResponseEntity.ok(this.scheduleService.findRequestSchedule(studentCourse, dayInWeek));
    }
}
