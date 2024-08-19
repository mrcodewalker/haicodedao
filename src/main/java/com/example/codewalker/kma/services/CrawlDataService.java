package com.example.codewalker.kma.services;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class CrawlDataService implements ICrawlDataService{
    private static final String LOGIN_URL = "https://ccvc.edu.vn/login";
    private static final String TARGET_URL = "https://ccvc.edu.vn/trac-nghiem/bai-thi/507/tron-cau-hoi-ngau-nhien-demo";

    @Override
    public ResponseEntity<?> login(String username, String password) throws IOException {
        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("code", "400", "message", "Missing Item"));
        }

        CookieStore cookieStore = new BasicCookieStore();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            // Gửi yêu cầu GET để lấy trang đăng nhập và token bảo mật
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
            loginPost.setConfig(requestConfig);

            HttpResponse loginPostResponse = httpClient.execute(loginPost);
            HttpEntity loginPostEntity = loginPostResponse.getEntity();
            String loginPostHtml = EntityUtils.toString(loginPostEntity);

            // Xử lý phản hồi đăng nhập để lấy cookies và token
            String xsrfToken = getXSRFTokenFromCookies(cookieStore);

            // Gửi yêu cầu POST đến URL mục tiêu để mô phỏng bấm nút
            List<NameValuePair> submitParameters = new ArrayList<>();
            // Nếu nút gửi các tham số khác, bạn có thể thêm chúng vào đây
            // Tạo yêu cầu GET
            HttpGet targetPageGet = new HttpGet(TARGET_URL);
            targetPageGet.setHeader("Cookie", getCookieHeader(cookieStore));
            targetPageGet.setHeader("X-XSRF-TOKEN", xsrfToken);

            HttpResponse targetPageResponse = httpClient.execute(targetPageGet);
            HttpEntity targetPageEntity = targetPageResponse.getEntity();
            String targetPageJson = EntityUtils.toString(targetPageEntity);


            // Trả về nội dung của phản hồi submit
            return ResponseEntity.ok(Map.of(
                    "code", "200",
                    "message", "Yêu cầu thành công"
//                    "data", submitResponseJson
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("code", "500", "message", "Error: " + e.getMessage()));
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
