package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.responses.PostResponse;

import java.util.List;

public interface IPostService {
    PostResponse createPost(PostDTO postDTO) throws DataNotFoundException;
    void deletePostById(Long id);
    Post findById(Long id) throws DataNotFoundException;
    PostResponse updatePost(PostDTO postDTO);
    List<PostResponse> getPostByAuthorId(Long authorId) throws DataNotFoundException;
}
