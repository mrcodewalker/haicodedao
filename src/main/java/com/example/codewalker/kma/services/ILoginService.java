package com.example.codewalker.kma.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ILoginService {
    ResponseEntity<?> login(String username, String password) throws IOException;
    String getLoginPayload(String username, String password);
    ResponseEntity<?> loginVirtualCalendar(String username, String password) throws IOException;

}
