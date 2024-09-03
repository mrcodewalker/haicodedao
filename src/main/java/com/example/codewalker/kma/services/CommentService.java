package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CommentDTO;
import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Comment;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.CommentRepository;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.CommentResponse;
import com.example.codewalker.kma.responses.PostResponse;
import jakarta.xml.ws.ServiceMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    @Override
    public CommentResponse createComment(CommentDTO commentDTO) throws DataNotFoundException {
        if (this.formData(commentDTO)==null){
            throw new DataNotFoundException("Can not create a new comment right now!");
        }
        return this.responseData(this.commentRepository
                .save(Objects.requireNonNull(this.formData(commentDTO))));
    }

    @Override
    public void deleteCommentById(Long id) {
        this.commentRepository.deleteById(id);
    }

    @Override
    public CommentResponse updatePost(CommentDTO commentDTO) {
        return null;
    }

    @Override
    public List<CommentResponse> getCommentsByAuthorId(Long authorId) throws DataNotFoundException {
        User user = this.userService.getUserById(authorId);
        List<Comment> result = this.commentRepository.findByAuthor(user);
        List<CommentResponse> ans = new ArrayList<>();
        for (Comment clone : result){
            ans.add(this.responseData(clone));
        }
        return ans;
    }
    private Comment formData(CommentDTO commentDTO) throws DataNotFoundException {
        if (this.postService.findById(commentDTO.getPostId())==null){
            throw new DataNotFoundException("Can not comment on this available post");
        }
        if (userService.userExistsById(Long.parseLong(commentDTO.getAuthorId()))) {
            return Comment.builder()
                    .content(commentDTO.getContent())
                    .post(this.postService.findById(commentDTO.getPostId()))
                    .author(this.userService.getUserById(Long.parseLong(commentDTO.getAuthorId())))
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        return null;
    }
    private CommentResponse responseData(Comment comment){
        return CommentResponse.builder()
                .commentId(comment.getCommentId()+"")
                .postId(comment.getPost().getPostId()+"")
                .content(comment.getContent())
                .authorName(comment.getAuthor().getUsername())
                .build();
    }
}
