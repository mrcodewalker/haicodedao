package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class PostController {
    private final PostService postService;
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) throws DataNotFoundException {
        return ResponseEntity.ok(this.postService.createPost(postDTO));
    }
}
