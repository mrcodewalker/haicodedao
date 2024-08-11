package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.ListSubjectsDTO;
import com.example.codewalker.kma.dtos.LoginDTO;
import com.example.codewalker.kma.services.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<?> loginVirtualCalendar(@RequestBody LoginDTO loginDTO, HttpSession session) throws IOException {
        return this.loginService.loginVirtualCalendar(loginDTO.getUsername(), loginDTO.getPassword(), session);
    }
    @PostMapping("/tool/register")
    public ResponseEntity<?> registerTool(@RequestBody ListSubjectsDTO subjects, HttpSession session) throws IOException {
        return this.loginService.registerTool(subjects.getSubjects(), session);
    }
}