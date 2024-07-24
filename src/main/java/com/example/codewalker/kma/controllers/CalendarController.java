package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.responses.CalendarResponse;
import com.example.codewalker.kma.dtos.DataDTO;
import com.example.codewalker.kma.responses.TimelineResponse;
import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/calendar")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
@RequiredArgsConstructor
public class CalendarController {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static final String TIME_ZONE_ID = "Asia/Ho_Chi_Minh"; // Change this to your local time zone
    private static final TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
    private static final TimeZone timeZone = registry.getTimeZone(TIME_ZONE_ID);

    public Long count = 0L;

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportCalendar(@RequestBody CalendarResponse calendarResponse) throws ParseException, IOException {
        this.count = 0L;
        Calendar calendar = new Calendar();
        ProdId prodId = new ProdId("-//KMA Legend//NONSGML v1.0//EN");
        Version version = new Version();
        calendar.add(prodId);
        version.setValue("2.0");
        calendar.add(version);
        calendar.add(new CalScale("GREGORIAN"));

        DataDTO dataDTO = calendarResponse.getDataDTO();
        List<TimelineResponse> schedules = dataDTO.getTimelineResponse();

        for (TimelineResponse entry : schedules) {
            String[] studyDays = entry.getStudyDays().split(" ");
            String[] lessons = entry.getLessons().split(" ");
            for (int i = 0; i < studyDays.length; i++) {
                Date studyDate = DATE_FORMAT.parse(studyDays[i]);
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

        switch (lessons) {
            case "1,2,3":
                startTime = "07:00";
                endTime = "09:25";
                break;
            case "4,5,6":
                startTime = "09:35";
                endTime = "12:00";
                break;
            case "7,8,9":
                startTime = "12:30";
                endTime = "14:55";
                break;
            case "10,11,12":
                startTime = "15:05";
                endTime = "17:30";
                break;
            case "13,14,15":
                startTime = "18:00";
                endTime = "21:00";
                break;
        }

        // Parse the start and end times
        java.util.Calendar startCalendar = java.util.Calendar.getInstance();
        startCalendar.setTime(studyDate);
        startCalendar.set(java.util.Calendar.HOUR_OF_DAY, TIME_FORMAT.parse(startTime).getHours());
        startCalendar.set(java.util.Calendar.MINUTE, TIME_FORMAT.parse(startTime).getMinutes());
        startCalendar.setTimeZone(timeZone);

        java.util.Calendar endCalendar = java.util.Calendar.getInstance();
        endCalendar.setTime(studyDate);
        endCalendar.set(java.util.Calendar.HOUR_OF_DAY, TIME_FORMAT.parse(endTime).getHours());
        endCalendar.set(java.util.Calendar.MINUTE, TIME_FORMAT.parse(endTime).getMinutes());
        endCalendar.setTimeZone(timeZone);

        // Create DateTime objects with time zone
        DateTime dtStart = new DateTime(startCalendar.getTime());
        dtStart.setTimeZone(timeZone);

        DateTime dtEnd = new DateTime(endCalendar.getTime());
        dtEnd.setTimeZone(timeZone);

        // Create DtStart and DtEnd
        event.add(new DtStart(dtStart.toInstant()));
        event.add(new DtEnd(dtEnd.toInstant()));
        event.add(new Description(String.format("Lessons: %s\nTeacher: %s", lessons, teacher)));
        event.add(new Location(location));
        event.add(new Uid(this.count++ + "@mr.codewalker.kma.legend"));

        // Add event to calendar
        calendar.add(event);
    }
}
