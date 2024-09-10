package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.dtos.UpdateDataDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.services.PostService;
import lombok.Getter;
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
    @GetMapping("/collect")
    public ResponseEntity<?> collectPost(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(this.postService.getAllPosts(
                page
        ));
    }
    @GetMapping("/collect/reverse")
    public ResponseEntity<?> collectPostReverse(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(this.postService.getAllPostDecrease(
                page
        ));
    }
    @GetMapping("/collect/popular")
    public ResponseEntity<?> collectPopularPost(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(this.postService.getAllPostPopular(
                page
        ));
    }
    @GetMapping("/collect/by/key")
    public ResponseEntity<?> collectPopularPost(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(this.postService.searchPosts(
                searchTerm, page
        ));
    }
    @GetMapping("/collect/popular/week")
    public ResponseEntity<?> collectPopularWeek(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(this.postService.popularThisWeek(
                page
        ));
    }
    @GetMapping("/collect/popular/all/times")
    public ResponseEntity<?> collectPopularAllTimes(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(this.postService.popularAllTimes(
                page
        ));
    }
    @GetMapping("/collect/no/replies")
    public ResponseEntity<?> collectNoReplies(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(this.postService.noRepliesYet(
                page
        ));
    }
    @GetMapping("/increase/{id}")
    public ResponseEntity<?> increaseView(
           @PathVariable Long id
    ) {
        this.postService.increaseView(id);
        return ResponseEntity.ok("Everything gonna be alright!");
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) throws DataNotFoundException {
        this.postService.deletePostById(id, authHeader);
        return ResponseEntity.ok(this.postService.deletePostById(id, authHeader));
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePost(
            @RequestBody UpdateDataDTO updateDataDTO,
            @RequestHeader("Authorization") String authHeader
    ) throws DataNotFoundException {
        return ResponseEntity.ok(this.postService.updatePost(
                updateDataDTO, authHeader
        ));
    }
}
