package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.ListSubjectsDTO;
import com.example.codewalker.kma.dtos.LoginDTO;
import com.example.codewalker.kma.responses.GoogleLoginResponse;
import com.example.codewalker.kma.services.LoginService;
import com.example.codewalker.kma.services.ScoreService;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/login")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final ScoreService scoreService;
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
    @GetMapping("/oauth2/callback")
    @PermitAll
    public ResponseEntity<?> currentUserGoogle(
            OAuth2AuthenticationToken oAuth2AuthenticationToken
    ){
        Map<String, Object> map = oAuth2AuthenticationToken.getPrincipal().getAttributes();

        return ResponseEntity.ok(GoogleLoginResponse.builder()
                 .picture((String) map.get("picture"))
                    .name((String) map.get("name"))
                    .email((String) map.get("email"))
                .build());
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getScoresByStudentCode(@PathVariable("id") String studentCode){
        return ResponseEntity.ok(scoreService.getScoreByStudentCode(studentCode));
    }
}