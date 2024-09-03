package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.LikeDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/likes")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/create")
    public ResponseEntity<?> createLike(@RequestBody LikeDTO likeDTO) throws DataNotFoundException {
        return ResponseEntity.ok(
                this.likeService.createLike(likeDTO)
        );
    }
}
