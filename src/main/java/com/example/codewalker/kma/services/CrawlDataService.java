package com.example.codewalker.kma.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CrawlDataService {

    private static final String LOGIN_URL = "https://ccvc.edu.vn/login";
    private static final String TARGET_URL = "https://ccvc.edu.vn/trac-nghiem/bai-thi/507/tron-cau-hoi-ngau-nhien-demo";
    private static final String CHECK_SESSION_URL = "https://ccvc.edu.vn/request-check-session";

    public ResponseEntity<?> performActions(String username, String password) throws IOException {
        CookieStore cookieStore = new BasicCookieStore();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            // Đăng nhập
            String xsrfToken = login(httpClient, cookieStore, username, password);

            if (xsrfToken == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("code", "500", "message", "Login failed or token not found"));
            }

            // Kiểm tra session
            if (!checkSession(httpClient, cookieStore, xsrfToken)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("code", "500", "message", "Session check failed"));
            }

            // Bấm nút "Bắt đầu thi"

            return startExam(TARGET_URL, httpClient, cookieStore, xsrfToken);


        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("code", "500", "message", "Error: " + e.getMessage()));
        }
    }

    private String login(CloseableHttpClient httpClient, CookieStore cookieStore, String username, String password) throws IOException {
        // Gửi yêu cầu GET để lấy trang đăng nhập và CSRF token
        HttpGet loginGet = new HttpGet(LOGIN_URL);
        HttpResponse loginGetResponse = httpClient.execute(loginGet);
        HttpEntity loginGetEntity = loginGetResponse.getEntity();
        String loginGetHtml = EntityUtils.toString(loginGetEntity);

        Document loginDoc = Jsoup.parse(loginGetHtml);
        String token = loginDoc.select("input[name=_token]").attr("value");

        // Tạo yêu cầu POST để đăng nhập
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("email", username));
        urlParameters.add(new BasicNameValuePair("password", md5(password)));
        urlParameters.add(new BasicNameValuePair("_token", token));
        urlParameters.add(new BasicNameValuePair("RememberMe", "true"));

        HttpPost loginPost = new HttpPost(LOGIN_URL);
        loginPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        loginPost.setConfig(RequestConfig.custom().build());

        HttpResponse loginPostResponse = httpClient.execute(loginPost);
        HttpEntity loginPostEntity = loginPostResponse.getEntity();
        String loginPostHtml = EntityUtils.toString(loginPostEntity);

        // Lấy XSRF Token từ cookies
        return getXSRFTokenFromCookies(cookieStore);
    }

    private boolean checkSession(CloseableHttpClient httpClient, CookieStore cookieStore, String xsrfToken) throws IOException {
        HttpGet sessionGet = new HttpGet(CHECK_SESSION_URL);
        sessionGet.setHeader("Cookie", getCookieHeader(cookieStore));
        sessionGet.setHeader("X-XSRF-TOKEN", xsrfToken);

        HttpResponse sessionResponse = httpClient.execute(sessionGet);
        HttpEntity sessionEntity = sessionResponse.getEntity();
        String sessionResponseHtml = EntityUtils.toString(sessionEntity);

        // Kiểm tra nếu response cho thấy session còn hoạt động
        // (Ví dụ: kiểm tra nội dung HTML hoặc status code)
        return sessionResponse.getStatusLine().getStatusCode() == 200;
    }

    public ResponseEntity<Map<String, Object>> startExam(String examUrl, CloseableHttpClient httpClient, CookieStore cookieStore, String xsrfToken) {
        try {
            // Xác thực phiên làm việc
            HttpGet checkSession = new HttpGet("https://ccvc.edu.vn/request-check-session");
            checkSession.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0");
            checkSession.setHeader("Cookie", getCookieHeader(cookieStore));

            HttpResponse sessionResponse = httpClient.execute(checkSession);
            String sessionResponseBody = EntityUtils.toString(sessionResponse.getEntity());

            // Kiểm tra phản hồi xác thực
            if (!sessionResponseBody.contains("\"status\":\"success\"")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("code", "401", "message", "Phiên làm việc không hợp lệ hoặc hết hạn"));
            }

            // Lấy trang thi
            HttpGet examPageGet = new HttpGet(examUrl);
            examPageGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0");
            examPageGet.setHeader("Cookie", getCookieHeader(cookieStore));
            examPageGet.setHeader("X-XSRF-TOKEN", xsrfToken);

            HttpResponse examPageResponse = httpClient.execute(examPageGet);
            HttpEntity examPageEntity = examPageResponse.getEntity();
            String examPageHtml = EntityUtils.toString(examPageEntity);

            // Phân tích HTML để lấy form chứa nút "Bắt đầu thi"
            Document examDoc = Jsoup.parse(examPageHtml);
            Element startButton = examDoc.select("input[type=submit][class=btn][value=Bắt đầu thi]").first();

            if (startButton != null) {
                // Lấy form chứa nút "Bắt đầu thi"
                Element form = startButton.parents().select("form").first();

                if (form != null) {
                    String formAction = form.attr("action");
                    String formMethod = form.attr("method").toUpperCase();

                    // Gửi yêu cầu POST để bấm nút "Bắt đầu thi"
                    List<NameValuePair> formParameters = new ArrayList<>();
                    for (Element input : form.select("input[name]")) {
                        formParameters.add(new BasicNameValuePair(input.attr("name"), input.attr("value")));
                    }

                    HttpPost formPost = new HttpPost(formAction);
                    formPost.setEntity(new UrlEncodedFormEntity(formParameters));
                    formPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0");
                    formPost.setHeader("Cookie", getCookieHeader(cookieStore));
                    formPost.setHeader("X-XSRF-TOKEN", xsrfToken);

                    HttpResponse formPostResponse = httpClient.execute(formPost);
                    HttpEntity formPostEntity = formPostResponse.getEntity();
                    String formPostHtml = EntityUtils.toString(formPostEntity);

                    // Tiếp tục xử lý nội dung trả về từ phản hồi sau khi bấm nút
                    Document postDoc = Jsoup.parse(formPostHtml);

                    return ResponseEntity.ok(Map.of(
                            "code", "200",
                            "message", "Đã bấm nút Bắt đầu thi thành công",
                            "data", postDoc.toString()
                    ));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("code", "500", "message", "Không tìm thấy form chứa nút Bắt đầu thi"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("code", "500", "message", "Không tìm thấy nút Bắt đầu thi"));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("code", "500", "message", "Error: " + e.getMessage()));
        }
    }
    private static String getXSRFToken(CloseableHttpClient httpClient) throws IOException {
        // Lấy XSRF-TOKEN từ trang chính
        HttpGet getToken = new HttpGet(TARGET_URL);
        try (CloseableHttpResponse response = httpClient.execute(getToken)) {
            String html = EntityUtils.toString(response.getEntity());
            Document doc = Jsoup.parse(html);
            Element tokenElement = doc.select("meta[name=_token]").first();
            return tokenElement != null ? tokenElement.attr("content") : "";
        }
    }

    private String md5(String input) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(input);
    }

    private String getCookieHeader(CookieStore cookieStore) {
        StringBuilder cookieHeader = new StringBuilder();
        cookieStore.getCookies().forEach(cookie -> {
            cookieHeader.append(cookie.getName())
                    .append("=")
                    .append(cookie.getValue())
                    .append("; ");
        });
        return cookieHeader.toString();
    }

    private String getXSRFTokenFromCookies(CookieStore cookieStore) {
        return cookieStore.getCookies().stream()
                .filter(cookie -> "XSRF-TOKEN".equals(cookie.getName()))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElse("");
    }
}
