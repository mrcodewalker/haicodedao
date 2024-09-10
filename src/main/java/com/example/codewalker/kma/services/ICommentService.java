package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CommentDTO;
import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.dtos.UpdateDataDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.responses.CommentResponse;
import com.example.codewalker.kma.responses.PostResponse;
import com.example.codewalker.kma.responses.StatusResponse;

import java.util.List;

public interface ICommentService {
    CommentResponse createComment(CommentDTO commentDTO) throws DataNotFoundException;
    StatusResponse deleteCommentById(Long id, String token);
    StatusResponse updateComment(UpdateDataDTO updateDataDTO, String token);
    List<CommentResponse> getCommentsByAuthorId(Long authorId) throws DataNotFoundException;
    List<CommentResponse> getCommentsByPostId(Long postId);
}
