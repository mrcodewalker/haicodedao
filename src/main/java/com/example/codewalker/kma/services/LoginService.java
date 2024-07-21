package com.example.codewalker.kma.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LoginService implements ILoginService{
    private static final String LOGIN_URL = "http://qldt.actvn.edu.vn/CMCSoft.IU.Web.Info/Login.aspx";
    private static final String STUDENT_PROFILE_URL = "http://qldt.actvn.edu.vn/CMCSoft.IU.Web.Info/StudentProfileNew/HoSoSinhVien.aspx";
    private static final String STUDENT_SCHEDULE_URL = "http://qldt.actvn.edu.vn/CMCSoft.IU.Web.Info/Reports/Form/StudentTimeTable.aspx";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    public ResponseEntity<?> login(String username, String password) throws IOException {
        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("code", "400", "message", "Missing Item"));
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet loginGet = new HttpGet(LOGIN_URL);
            HttpResponse loginGetResponse = httpClient.execute(loginGet);
            HttpEntity loginGetEntity = loginGetResponse.getEntity();
            String loginGetHtml = EntityUtils.toString(loginGetEntity);

            Document loginDoc = Jsoup.parse(loginGetHtml);
            Map<String, String> formData = parseInitialFormData(loginDoc);
            formData.put("txtUserName", username);
            formData.put("txtPassword", md5(password));
            formData.put("btnSubmit", "Đăng nhập");

            List<NameValuePair> urlParameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            HttpPost loginPost = new HttpPost(LOGIN_URL);
            HttpEntity formEntity = new UrlEncodedFormEntity(urlParameters);
            loginPost.setEntity(formEntity);

            HttpResponse loginPostResponse = httpClient.execute(loginPost);
            HttpEntity loginPostEntity = loginPostResponse.getEntity();
            String loginPostHtml = EntityUtils.toString(loginPostEntity);

            Document postLoginDoc = Jsoup.parse(loginPostHtml);
            String wrongPass = postLoginDoc.select("#lblErrorInfo").text();

            if ("Bạn đã nhập sai tên hoặc mật khẩu!".equals(wrongPass) || "Tên đăng nhập không đúng!".equals(wrongPass)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("code", "401", "message", "Wrong Password"));
            }

            HttpGet profileGet = new HttpGet(STUDENT_PROFILE_URL);
            HttpResponse profileGetResponse = httpClient.execute(profileGet);
            HttpEntity profileGetEntity = profileGetResponse.getEntity();
            String profileGetHtml = EntityUtils.toString(profileGetEntity);

            Document profileDoc = Jsoup.parse(profileGetHtml);
            String displayName = profileDoc.select("input[name=txtHoDem]").val() + " " + profileDoc.select("input[name=txtTen]").val();
            String studentCode = profileDoc.select("input[name=txtMaSV]").val();
            String gender = profileDoc.select("select[name=drpGioiTinh] option[selected]").text();
            String birthday = profileDoc.select("input[name=txtNgaySinh]").val();

            Map<String, String> studentInfo = new HashMap<>();
            studentInfo.put("displayName", displayName);
            studentInfo.put("studentCode", studentCode);
            studentInfo.put("gender", gender);
            studentInfo.put("birthday", birthday);

            HttpGet scheduleGet = new HttpGet(STUDENT_SCHEDULE_URL);
            HttpResponse scheduleGetResponse = httpClient.execute(scheduleGet);
            HttpEntity scheduleGetEntity = scheduleGetResponse.getEntity();
            String scheduleGetHtml = EntityUtils.toString(scheduleGetEntity);

            Document scheduleDoc = Jsoup.parse(scheduleGetHtml);
            Elements scheduleRows = scheduleDoc.select("tr.cssListItem, tr.cssListAlternativeItem");

            List<Map<String, String>> scheduleData = new ArrayList<>();
            for (Element row : scheduleRows) {
                Map<String, String> rowData = new HashMap<>();
                rowData.put("courseName", row.select("td").get(1).text());
                rowData.put("courseCode", row.select("td").get(2).text());
//                rowData.put("studySchedule", row.select("td").get(3).html().replace("<br>", "\n"));
                Map<String, String> parsedSchedule = parseSchedule(row.select("td").get(3).html().replace("<br>", "\n"));
                rowData.put("days", parsedSchedule.get("days"));
                rowData.put("lessons", parsedSchedule.get("lessons"));
                rowData.put("studyLocation", row.select("td").get(4).text());
                rowData.put("teacher", row.select("td").get(5).text());
                scheduleData.add(rowData);
            }

            return ResponseEntity.ok(Map.of(
                    "code", "200",
                    "message", "OK",
                    "data", Map.of(
                            "studentInfo", studentInfo,
                            "studentSchedule", scheduleData
                    )
            ));

        } catch (IOException | ParseException e) {
//            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("code", "500", "message", "Error: " + e.getMessage()));
        }
    }

    public String getLoginPayload(String username, String password) {
        StringBuilder payload = new StringBuilder();
        payload.append("txtUserName=").append(username)
                .append("&txtPassword=").append(password)
                .append("&btnSubmit=Đăng+nhập");
        return payload.toString();
    }
    private Map<String, String> parseInitialFormData(Document doc) {
        Map<String, String> formData = new HashMap<>();
        for (Element input : doc.select("input")) {
            formData.put(input.attr("name"), input.attr("value"));
        }
        return formData;
    }

    private String md5(String input) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(input);
    }
    private Map<String, String> parseSchedule(String studySchedule) throws ParseException {
        // Loại bỏ các thẻ HTML bằng cách sử dụng Jsoup để lấy văn bản thuần túy
        Document doc = Jsoup.parse(studySchedule);
        String cleanText = doc.body().text();
        String[] lines = studySchedule.split("\n");
        // Tách các đoạn văn bản bằng cách phân tách theo các ký tự xuống dòng
        List<String> startDay = new ArrayList<>();
        List<String> endDay = new ArrayList<>();
        List<String> lesson = new ArrayList<>();
        List<String> dayInWeek = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
        int check = 0;
        String start = "";
        String end = "";
        for (String line : lines){
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (line.contains("Từ")||
                    line.contains("đến")) {
                line = line.substring(0, line.indexOf(":"));
                start = "";
                end = "";
                check=1;
            } else {
                int i = 1 + line.indexOf(">");
                int j = line.lastIndexOf("<");
                line = line.substring(i, j);
                check=0;
            }
            String[] parts = line.split(" ");
            int day = 2;
            if (check==1){
                start=parts[1];
                end=parts[3];
            } else {
                day = Integer.parseInt(parts[1]);
                dayInWeek.add(parts[1]);
                Date starts = sdf.parse(start);
                Date ends = sdf.parse(end);
                calendar.setTime(starts);
                while (!calendar.getTime().after(ends)) {
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == dayOfWeekToCalendarDay(day+"")) {
                        String dateStr = sdf.format(calendar.getTime());
                        if (result.get("days")==null){
                            result.put("days", dateStr);
                            result.put("lessons", parts[3]);

                        } else {
                            result.put("days", result.get("days")+" "+dateStr);
                            result.put("lessons", result.get("lessons")+" "+parts[3]);
                        }
                    }
                    calendar.add(Calendar.DATE, 1);
                }
            }
        }
        return result;
    }

    private int dayOfWeekToCalendarDay(String dayOfWeek) {
        switch (dayOfWeek) {
            case "2": return Calendar.MONDAY;
            case "3": return Calendar.TUESDAY;
            case "4": return Calendar.WEDNESDAY;
            case "5": return Calendar.THURSDAY;
            case "6": return Calendar.FRIDAY;
            case "7": return Calendar.SATURDAY;
            default: return Calendar.SUNDAY;
        }
    }
    private static int getDayOfWeek(String line) {
        if (line.contains("Thứ 2")) return Calendar.MONDAY;
        if (line.contains("Thứ 3")) return Calendar.TUESDAY;
        if (line.contains("Thứ 4")) return Calendar.WEDNESDAY;
        if (line.contains("Thứ 5")) return Calendar.THURSDAY;
        if (line.contains("Thứ 6")) return Calendar.FRIDAY;
        if (line.contains("Thứ 7")) return Calendar.SATURDAY;
        if (line.contains("Chủ nhật")) return Calendar.SUNDAY;
        return -1;
    }
}
