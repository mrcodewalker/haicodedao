package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.LoginDTO;
import com.example.codewalker.kma.dtos.UserDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.responses.ProfileResponse;
import com.example.codewalker.kma.responses.RegisterUserResponse;
import com.example.codewalker.kma.responses.StatusResponse;
import com.example.codewalker.kma.services.UserService;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(this.userService.createUser(userDTO));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws Exception {
        return ResponseEntity.ok(this.userService.login(loginDTO.getUsername(), loginDTO.getPassword()));
    }
    @GetMapping("/find/{username}")
    public ResponseEntity<?> findByUserName(@PathVariable("username") String username){
        return ResponseEntity.ok(this.userService.checkUserName(username));
    }
    @GetMapping("/oauth2/user")
    public Principal user(Principal principal){
        return principal;
    }
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfileFromId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) throws DataNotFoundException {
        ProfileResponse profileResponse = this.userService.getUserProfile(id+"", authHeader);
        if (profileResponse == null ||profileResponse.getUsername().length()<6){
            return ResponseEntity.ok(RegisterUserResponse.builder()
                            .userName("null")
                    .build());
        };
        return ResponseEntity.ok(this.userService.getUserProfile(id+"", authHeader));
    }
    @GetMapping("/query/{id}")
    public ResponseEntity<?> updateAvatar(
            @PathVariable Long id,
            @RequestParam String avatar,
            @RequestHeader("Authorization") String authHeader
    ) throws DataNotFoundException {
        StatusResponse statusResponse = this.userService.updateAvatar(id+"",avatar, authHeader);
        return ResponseEntity.ok(statusResponse);
    }
    @GetMapping("/collect/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> collectUsers(
    ) throws DataNotFoundException {
        return ResponseEntity.ok(this.userService.collectUsers());
    }
}
