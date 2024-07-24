package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.responses.CalendarResponse;
import com.example.codewalker.kma.dtos.DataDTO;
import com.example.codewalker.kma.responses.TimelineResponse;
import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/calendar")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
@RequiredArgsConstructor
public class CalendarController {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public Long count = 0L;

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportCalendar(@RequestBody CalendarResponse calendarResponse) throws ParseException, IOException {
        Calendar calendar = new Calendar();
        Version version = new Version();
        version.setValue("2.0");
        calendar.add(version);
        calendar.add(new CalScale("GREGORIAN"));
        DataDTO dataDTO = calendarResponse.getDataDTO();
        List<TimelineResponse> schedules = dataDTO.getTimelineResponse();

        for (TimelineResponse entry : schedules) {
            String line[] = entry.getStudyDays().split(" ");
            for (int i=0;i<line.length;i++) {
                Date studyDate = DATE_FORMAT.parse(line[i]);
                String[] lessons = entry.getLessons().split(" ");
                addEvent(calendar, entry.getCourseName(), studyDate, lessons[i], entry.getStudyLocation(), entry.getTeacher());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, baos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/calendar"));
        headers.setContentDispositionFormData("attachment", "student_schedule.ics");

        return ResponseEntity.ok()
                .headers(headers)
                .body(baos.toByteArray());
    }

    private void addEvent(Calendar calendar, String courseName, Date studyDate, String lessons, String location, String teacher) throws ParseException {
        // Create a VEvent
        VEvent event = new VEvent();

        // Set the event summary
        Summary summary = new Summary(courseName);
        event.add(summary);


        // Convert lessons to time ranges
        String startTime = "07:00"; // Default start time
        String endTime = "09:25"; // Default end time

        if (lessons.startsWith("4")) {
            startTime = "09:35";
            endTime = "12:00";
        } else if (lessons.startsWith("7")) {
            startTime = "12:30";
            endTime = "14:55";
        } else if (lessons.startsWith("10")) {
            startTime = "15:05";
            endTime = "17:30";
        } else if (lessons.startsWith("13")) {
            startTime = "18:00";
            endTime = "21:00";
        }

        // Create DateTime objects
        DateTime dtStart = new DateTime(ZonedDateTime.ofInstant(studyDate.toInstant(), ZoneId.systemDefault()).toInstant().toEpochMilli());
        dtStart.setHours(TIME_FORMAT.parse(startTime).getHours());
        dtStart.setMinutes(TIME_FORMAT.parse(startTime).getMinutes());

        DateTime dtEnd = new DateTime(ZonedDateTime.ofInstant(studyDate.toInstant(), ZoneId.systemDefault()).toInstant().toEpochMilli());
        dtEnd.setHours(TIME_FORMAT.parse(endTime).getHours());
        dtEnd.setMinutes(TIME_FORMAT.parse(endTime).getMinutes());

        // Create DtStart and DtEnd
        event.add(new DtStart(dtStart.toInstant()));
        event.add(new DtEnd(dtEnd.toInstant()));
        event.add(new Description(String.format("Lessons: %s\nTeacher: %s", lessons, teacher)));
        event.add(new Location(location));
        event.add(new Uid(this.count++ +"@mr.codewalker.kma.legend"));

        // Add event to calendar
        calendar.add(event);
    }
}
