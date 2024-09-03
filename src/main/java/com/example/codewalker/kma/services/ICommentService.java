package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CommentDTO;
import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.responses.CommentResponse;
import com.example.codewalker.kma.responses.PostResponse;

import java.util.List;

public interface ICommentService {
    CommentResponse createComment(CommentDTO commentDTO) throws DataNotFoundException;
    void deleteCommentById(Long id);
    CommentResponse updatePost(CommentDTO commentDTO);
    List<CommentResponse> getCommentsByAuthorId(Long authorId) throws DataNotFoundException;
}
