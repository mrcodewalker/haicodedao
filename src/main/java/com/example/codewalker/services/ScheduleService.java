package com.example.codewalker.services;

import com.example.codewalker.models.Schedule;
import com.example.codewalker.models.Subject;
import com.example.codewalker.repositories.ScheduleRepository;
import com.example.codewalker.repositories.SubjectRepository;
import com.example.codewalker.responses.*;
import com.example.codewalker.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService{
    private final ScheduleRepository scheduleRepository;
    private final SubjectRepository subjectRepository;
    private List<Date> scheduleEnd = new ArrayList<>();
    @Override
    public Schedule createSchedule(Schedule schedule) {
        if (scheduleRepository.findAll().contains(schedule)){
            return null;
        }
        List<Subject> subjectList = this.subjectRepository.findAll();
        List<String> subjectNames = new ArrayList<>();
        for (Subject clone : subjectList){
            subjectNames.add(clone.getSubjectName());
        }
        String validSubjectName = schedule.getSubjectName();
        int index = schedule.getSubjectName().indexOf("-");
        if (index != -1) {
            int lastIndex = schedule.getSubjectName().indexOf("-", index + 1);
            if (lastIndex != -1 && lastIndex-index>=3) {
                validSubjectName = schedule.getSubjectName().substring(0, lastIndex);
            } else {
                if (lastIndex!=-1 && lastIndex-index<3){
                    validSubjectName = schedule.getSubjectName().substring(0, index);
                }
            }
        }

        for (String subjectName : subjectNames){
            if (subjectName.equals(validSubjectName)
            || subjectName.equalsIgnoreCase(validSubjectName)
                ||  Normalizer.normalize(subjectName, Normalizer.Form.NFD).replaceAll("\\p{M}", "").equalsIgnoreCase(
                    Normalizer.normalize(validSubjectName, Normalizer.Form.NFD).replaceAll("\\p{M}", "")))   {
                Subject subject = this.subjectRepository.findFirstBySubjectName(subjectName);
                subject.setSubjectCredits(schedule.getSubjectCredits());
                subject.setId(subject.getId());
                this.subjectRepository.save(subject);
                return this.scheduleRepository.save(schedule);
            }
        }
        this.subjectRepository.save(
                Subject.builder()
                        .subjectName(validSubjectName)
                        .subjectCredits(schedule.getSubjectCredits())
                        .build()
        );
        return this.scheduleRepository.save(schedule);
    }

    @Override
    public List<ScheduleResponse> findByStudentCourse(String studentCourse) {
        List<Schedule> list = this.scheduleRepository.findBySubjectNameContainingKeyword(studentCourse);
        List<ScheduleResponse> ans = new ArrayList<>();
        for (Schedule schedule : list){
            ScheduleResponse response = ScheduleResponse.builder()
                    .lessonNumber(schedule.getLessonNumber())
                    .dayInWeek(schedule.getDayInWeek())
                    .subjectCredits(schedule.getSubjectCredits())
                    .endDay(schedule.getEndDay())
                    .startDay(schedule.getStartDay())
                    .subjectName(schedule.getSubjectName())
                    .build();
            ans.add(response);
        }
        return ans;
    }

    @Override
    public List<SubjectResponse> findAllSubjects(String studentCourse) {
        List<Schedule> list = this.scheduleRepository.findBySubjectNameContainingKeyword(studentCourse);
        Set<String> subjectName = new HashSet<>();
        for (Schedule schedule: list){
            subjectName.add(schedule.getSubjectName().trim());
        }
        List<SubjectResponse> subjectResponses = new ArrayList<>();
        for (String collect : subjectName){
            subjectResponses.add(
                    SubjectResponse.builder()
                            .subjectName(collect)
                            .build()
            );
        }
        Collections.sort(subjectResponses, Comparator.comparing(SubjectResponse::getSubjectName));
        return subjectResponses;
    }
    public String convertTime(String lessonNumber){
        String line[] = lessonNumber.split("->");
        String response = "";
        int cnt = 0;
        for (String clone: line){
            cnt++;
            if (cnt!=1){
                response+="->";
            }
            switch (clone){
                case "1":
                    if (cnt==1){
                        response+="7:00";
                    } else {
                        response+="7:45";
                    }
                    break;
                case "2":
                    if (cnt==1){
                        response+="7:50";
                    } else {
                        response+="8:35";
                    }
                    break;
                case "3":
                    if (cnt==1){
                        response+="8:40";
                    } else {
                        response+="9:25";
                    }
                    break;
                case "4":
                    if (cnt==1){
                        response+="9:35";
                    } else {
                        response+="10:20";
                    }
                    break;
                case "5":
                    if (cnt==1){
                        response+="10:25";
                    } else {
                        response+="11:10";
                    }
                    break;
                case "6":
                    if (cnt==1){
                        response+="11:15";
                    } else {
                        response+="12:00";
                    }
                    break;
                case "7":
                    if (cnt==1){
                        response+="12:30";
                    } else {
                        response+="13:15";
                    }
                    break;
                case "8":
                    if (cnt==1){
                        response+="13:20";
                    } else {
                        response+="14:05";
                    }
                    break;
                case "9":
                    if (cnt==1){
                        response+="14:10";
                    } else {
                        response+="14:55";
                    }
                    break;
                case "10":
                    if (cnt==1){
                        response+="15:05";
                    } else {
                        response+="15:50";
                    }
                    break;
                case "11":
                    if (cnt==1){
                        response+="15:55";
                    } else {
                        response+="16:40";
                    }
                    break;
                case "12":
                    if (cnt==1){
                        response+="16:45";
                    } else {
                        response+="17:30";
                    }
                    break;
                case "13":
                    if (cnt==1){
                        response+="17:35";
                    } else {
                        response+="18:20";
                    }
                    break;
                case "14":
                    if (cnt==1){
                        response+="18:25";
                    } else {
                        response+="19:10";
                    }
                    break;
                case "15":
                    if (cnt==1){
                        response+="19:15";
                    } else {
                        response+="20:00";
                    }
                    break;
                case "16":
                    if (cnt==1){
                        response+="20:05";
                    } else {
                        response+="20:50";
                    }
                    break;
                case "17":
                    if (cnt==1){
                        response+="20:55";
                    } else {
                        response+="21:40";
                    }
                    break;
            }
        }
        return response;
    }

    @Override
    public SubjectDetailResponse findScheduleTime(String subjectName) {
        List<Schedule> list = this.scheduleRepository.findBySubjectNameContainingKeyword(subjectName);
        Long subjectCredits = 0L;
        List<Long> dayInWeek = new ArrayList<>();
        List<Date> startDay = new ArrayList<>();
        List<Date> endDay = new ArrayList<>();
        List<String> lessonNumber = new ArrayList<>();
        for (Schedule schedule : list){
            if (schedule.getSubjectName().equals(subjectName)
            || schedule.getSubjectName().equalsIgnoreCase(subjectName.toLowerCase())
            || Normalizer.normalize(schedule.getSubjectName().trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").equalsIgnoreCase(
                    Normalizer.normalize(subjectName.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", ""))){
                subjectCredits = schedule.getSubjectCredits();
                dayInWeek.add(schedule.getDayInWeek());
                lessonNumber.add(this.convertTime(schedule.getLessonNumber()));
                startDay.add(schedule.getStartDay());
                endDay.add(schedule.getEndDay());
            }
        }
//        System.out.println(this.findMatchingDays(startDay, endDay, dayInWeek, lessonNumber));
        return SubjectDetailResponse.builder()
                .subjectName(subjectName)
                .dayInWeek(dayInWeek)
                .endDay(endDay)
                .startDay(startDay)
                .lessonNumber(lessonNumber)
                .subjectCredits(subjectCredits)
                .schedule(this.findMatchingDays(startDay, endDay, dayInWeek, lessonNumber))
                .scheduleEnd(this.scheduleEnd)
                .build();
    }

    @Override
    public WannaResponse findRequestSchedule(String studentCourse, List<String> dayInWeek) {
        List<Long> day = new ArrayList<>();
        Set<String> subjects = new HashSet<>();
        Set<Date> invalidDate = new HashSet<>();
        List<Schedule> schedules = this.scheduleRepository.findBySubjectNameContainingKeyword(studentCourse);
        for (int i=0;i<schedules.size();i++){
            String subjectName = schedules.get(i).getSubjectName().substring(0,schedules.get(i).getSubjectName().indexOf("(")).trim();
            subjects.add(subjectName);
        }
        for (int i=0;i<dayInWeek.size();i++){
            if (dayInWeek.get(i).equalsIgnoreCase("monday")){
                day.add(2L);
            } else {
                if (dayInWeek.get(i).equalsIgnoreCase("tuesday")){
                    day.add(3L);
                } else {
                    if (dayInWeek.get(i).equalsIgnoreCase("wednesday")){
                        day.add(4L);
                    }
                    else {
                        if (dayInWeek.get(i).equalsIgnoreCase("thursday")){
                            day.add(5L);
                        } else {
                            if (dayInWeek.get(i).equalsIgnoreCase("friday")){
                                day.add(6L);
                            } else {
                                if (dayInWeek.get(i).equalsIgnoreCase("saturday")){
                                    day.add(7L);
                                }
                            }
                        }
                    }
                }
            }
        }
        List<SubjectDetailResponse> detailResponses = new ArrayList<>();
        List<SubjectDetailResponse> responses = new ArrayList<>();
        List<String> suggest = new ArrayList<>();
        for (String clone : subjects){
            List<Schedule> list = this.scheduleRepository.findBySubjectNameContainingKeyword(clone);
            String target = "";
            Set<String> temp = new HashSet<>();
            Set<Date> notMatch = new HashSet<>();
            for (int i=0;i < list.size();i++){
                for (int j=0;j<day.size();j++){
                    if (list.get(i).getDayInWeek().equals(day.get(j))){
                        temp.add(list.get(i).getSubjectName());
                        break;
                    }
                }
            }
            int cnt = 0;
            if (responses.size()==1){
                invalidDate.addAll(responses.get(0).getSchedule().getSchedule());
            }
            boolean errorFound = false;
            for (String each: temp){
                SubjectDetailResponse detailResponse = this.findScheduleTime(each);
                for (Date date: detailResponse.getSchedule().getSchedule()){
                    if (invalidDate.contains(date)){
                        errorFound = true;
                        break;
                    }
                }
                if (!errorFound && cnt==0){
                    responses.add(detailResponse);
                }
                if (errorFound||cnt!=0){
                    suggest.add(detailResponse.getSubjectName());
                }
                cnt++;
            }
        }
        return WannaResponse.builder()
                .subjectDetailResponse(responses)
                .subjects(suggest)
                .build();
    }

    private int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    private boolean isMatchingDayOfWeek(Date date, long dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return currentDayOfWeek == dayOfWeek;
    }
    private DateResponse findMatchingDays(List<Date> startDay, List<Date> endDay, List<Long> dayInWeek, List<String> lessonNumber) {
        List<Date> matchingDays = new ArrayList<>();
        List<Date> scheduleEnd = new ArrayList<>(); // Danh sách thời gian kết thúc mới
        List<DateResponse> list = new ArrayList<>();
        List<Long> day = new ArrayList<>();
        // Lặp qua từng cặp startDay và endDay
        for (int i = 0; i < startDay.size(); i++) {
            Date currentDate = startDay.get(i); // Bắt đầu từ ngày bắt đầu
            Date endDate = endDay.get(i); // Ngày kết thúc cho cặp này
            String[] lessonTimes = lessonNumber.get(i).split("->");
            String startTime = lessonTimes[0]; // Thời gian bắt đầu từ lessonNumber
            String endTime = lessonTimes[1]; // Thời gian kết thúc từ lessonNumber

            // Lặp qua từng ngày từ startDay đến endDay
            while (!currentDate.after(endDate)) {
                // Kiểm tra xem ngày hiện tại có trùng với thứ trong dayInWeek không
                if (isMatchingDayOfWeek(currentDate, dayInWeek.get(i))) {
                    // Tạo ngày mới với thời gian bắt đầu từ lessonNumber
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    String[] startTimeParts = startTime.split(":");
                    int startHour = Integer.parseInt(startTimeParts[0]);
                    int startMinute = Integer.parseInt(startTimeParts[1]);
                    calendar.set(Calendar.HOUR_OF_DAY, startHour);
                    calendar.set(Calendar.MINUTE, startMinute);
                    matchingDays.add(calendar.getTime()); // Thêm vào danh sách

                    day.add(dayInWeek.get(i));

                    // Tạo ngày mới với thời gian kết thúc từ lessonNumber
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(currentDate);
                    String[] endTimeParts = endTime.split(":");
                    int endHour = Integer.parseInt(endTimeParts[0]);
                    int endMinute = Integer.parseInt(endTimeParts[1]);
                    endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
                    endCalendar.set(Calendar.MINUTE, endMinute);
                    scheduleEnd.add(endCalendar.getTime()); // Thêm vào danh sách
                }
                // Tăng ngày lên 1
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DATE, 1);
                currentDate = calendar.getTime();
            }
        }
        this.scheduleEnd = scheduleEnd; // Gán danh sách thời gian kết thúc vào this.scheduleEnd
//        Collections.sort(this.scheduleEnd);
        Collections.sort(matchingDays);
        Collections.sort(scheduleEnd);
        return DateResponse.builder()
                .schedule(matchingDays)
                .dayInWeek(day)
                .build();
    }


}
