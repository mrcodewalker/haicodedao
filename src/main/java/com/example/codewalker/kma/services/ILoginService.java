package com.example.codewalker.kma.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ILoginService {
    ResponseEntity<?> login(String username, String password) throws IOException;
    String getLoginPayload(String username, String password);
    ResponseEntity<?> loginVirtualCalendar(String username, String password, HttpSession session) throws IOException;
    ResponseEntity<?> registerTool(List<String> subjects, HttpSession session) throws IOException;

}
