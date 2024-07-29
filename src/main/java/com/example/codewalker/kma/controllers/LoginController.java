package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.LoginDTO;
import com.example.codewalker.kma.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/login")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws IOException {
        return this.loginService.login(loginDTO.getUsername(), loginDTO.getPassword());
    }
    @PostMapping("/login/virtual_calendar")
    public ResponseEntity<?> loginVirtualCalendar(@RequestBody LoginDTO loginDTO) throws IOException {
        return this.loginService.loginVirtualCalendar(loginDTO.getUsername(), loginDTO.getPassword());
    }
}