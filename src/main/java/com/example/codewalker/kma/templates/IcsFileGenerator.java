package com.example.codewalker.kma.templates;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class IcsFileGenerator {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static final String TIME_ZONE_ID = "Asia/Ho_Chi_Minh"; // Change this to your local time zone
//    private static final TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
//    private static final net.fortuna.ical4j.model.TimeZone timeZone = registry.getTimeZone(TIME_ZONE_ID);

    public Long count = 0L;
    public static void main(String[] args) throws Exception {
        Calendar calendar = new Calendar();
        ProdId prodId = new ProdId("-//Hoàng Minh Anh x Mr.CodeWalker//NONSGML v1.0//EN");
        calendar.add(prodId);
        Version version = new Version();
        version.setValue("2.0");
        calendar.add(version);
        calendar.add(new CalScale("GREGORIAN"));

        // Luật La Mã (CIL2001)
        addRecurringEvent(calendar, "Luật La Mã", "CIL2001", "NA-102", "PGS.TS. Phan Quốc Nguyên",
                "20240909", "20241101", new int[]{2}, new int[]{2, 3, 4, 5});

        // Lý luận về nhà nước và pháp luật (THL1052)
        addRecurringEvent(calendar, "Lý luận về nhà nước và pháp luật", "THL1052", "NA-102", "ThS. Nguyễn Thị Hoài Phương",
                "20241104", "20241129", new int[]{2, 5}, new int[]{2, 3, 4, 5});

        // Triết học Mác - Lênin (PHI1006)
        addRecurringEvent(calendar, "Triết học Mác - Lênin", "PHI1006", "NA-102", "NA",
                "20240909", "20241101", new int[]{3}, new int[]{2, 3, 4, 5});

        // Logic học đại cương (PHI1051)
        addRecurringEvent(calendar, "Logic học đại cương", "PHI1051", "NA-102", "NA",
                "20240909", "20241101", new int[]{4}, new int[]{2, 3, 4, 5});

        // Luật hiến pháp (CAL2001) - Thứ 4
        addRecurringEvent(calendar, "Luật hiến pháp", "CAL2001", "NA-102", "TS. Lã Khánh Tùng",
                "20241104", "20241129", new int[]{4}, new int[]{2, 3, 4, 5});

        // Luật hiến pháp (CAL2001) - Thứ 6
        addRecurringEvent(calendar, "Luật hiến pháp", "CAL2001", "NA-102", "TS. Lã Khánh Tùng",
                "20240909", "20241129", new int[]{6}, new int[]{2, 3, 4, 5});

        // Sinh hoạt lớp (SH)
        addRecurringEvent(calendar, "Sinh hoạt lớp", "SH", "GV. Chủ nhiệm", "NA",
                "20240909", "20241129", new int[]{5}, new int[]{10});

        // Lưu file ICS
        try (FileOutputStream fout = new FileOutputStream("C:\\Users\\ADMIN\\MyWebsite\\codewalker.kma\\codewalker.kma\\scheduless.ics")) {
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        }
    }

    private static void addRecurringEvent(Calendar calendar, String title, String courseCode, String location, String instructor,
                                          String startDate, String endDate, int[] daysOfWeek, int[] periods) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        TimeZone timezone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

        java.util.Calendar startCal = new GregorianCalendar();
        startCal.setTime(dateFormat.parse(startDate + "T000000"));
        java.util.Calendar endCal = new GregorianCalendar();
        endCal.setTime(dateFormat.parse(endDate + "T000000"));

        while (!startCal.after(endCal)) {
            for (int day : daysOfWeek) {
                java.util.Calendar eventStart = (java.util.Calendar) startCal.clone();
                eventStart.set(java.util.Calendar.DAY_OF_WEEK, day);

                // Thiết lập thời gian bắt đầu cho tiết đầu tiên
                setEventTime(eventStart, periods[0], timezone);

                // Thiết lập thời gian kết thúc cho tiết cuối cùng
                java.util.Calendar eventEnd = (java.util.Calendar) eventStart.clone();
                setEventTime(eventEnd, periods[periods.length - 1], timezone);
                eventEnd.add(java.util.Calendar.MINUTE, 50); // Thêm 50 phút cho tiết cuối cùng

                // Tạo sự kiện
                DateTime start = new DateTime(eventStart.getTime());
                DateTime end = new DateTime(eventEnd.getTime());
                VEvent event = new VEvent(start.toInstant(), end.toInstant(), title);

                // Thiết lập location, description, and other properties
                event.add(new Description("Giảng đường: NA-102\nLớp: K69A1\n"+"Mã học phần: " + courseCode + "\nGiảng viên: " + instructor));
                event.add(new Uid(UUID.randomUUID().toString()));
                event.add(new Location(location));

                // Thêm sự kiện vào calendar
                calendar.add(event);
            }
            startCal.add(java.util.Calendar.WEEK_OF_YEAR, 1);
        }
    }


    private static void setEventTime(java.util.Calendar calendar, int period, TimeZone timezone) {
        switch (period) {
            case 1:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 7);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 2:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 8);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 3:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 9);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 4:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 10);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 5:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 11);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 6:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 13);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 7:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 14);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 8:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 15);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 9:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 16);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
            case 10:
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 17);
                calendar.set(java.util.Calendar.MINUTE, 0);
                break;
        }
    }
}
