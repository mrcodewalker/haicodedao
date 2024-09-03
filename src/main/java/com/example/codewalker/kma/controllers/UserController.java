package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.LoginDTO;
import com.example.codewalker.kma.dtos.UserDTO;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
