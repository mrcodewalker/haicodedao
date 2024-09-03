package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.CommentDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
@CrossOrigin(origins = "https://kma-legend.onrender.com")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) throws DataNotFoundException {
        return ResponseEntity.ok(
                this.commentService.createComment(commentDTO)
        );
    }
}
