package com.example.codewalker.kma.services;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ICrawlDataService {
    ResponseEntity<?> login(String username, String password) throws IOException;
}
