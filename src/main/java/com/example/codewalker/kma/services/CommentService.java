package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CommentDTO;
import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.dtos.UpdateDataDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.filters.JwtTokenProvider;
import com.example.codewalker.kma.models.Comment;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.CommentRepository;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.CommentResponse;
import com.example.codewalker.kma.responses.PostResponse;
import com.example.codewalker.kma.responses.StatusResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.xml.ws.ServiceMode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    @Value("${jwt.secret}")
    private String jwtSecret;
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
    public StatusResponse deleteCommentById(Long id, String token) {
        Comment comment = this.commentRepository.findCommentByIdWithAuthor(id);
        if (comment==null){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        if (!this.getUsernameFromToken(token.substring(7))
                .equalsIgnoreCase(comment.getAuthor().getUsername())
        ){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        comment.setIsActive(0L);
        this.commentRepository.save(comment);
        return StatusResponse.builder()
                .status("200")
                .build();
    }

    @Override
    public StatusResponse updateComment(UpdateDataDTO updateDataDTO, String token) {
        Comment comment = this.commentRepository.findCommentByIdWithAuthor(updateDataDTO.getId());
        if (comment==null){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        if (!this.getUsernameFromToken(token.substring(7))
                .equalsIgnoreCase(comment.getAuthor().getUsername())
        ){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        comment.setContent(updateDataDTO.getContent());
        this.commentRepository.save(comment);
        return StatusResponse.builder()
                .status("200")
                .build();
    }

    @Override
    public List<CommentResponse> getCommentsByAuthorId(Long authorId) throws DataNotFoundException {
        User user = this.userService.getUserById(authorId);
        List<Comment> result = this.commentRepository.findByAuthor(user);
        List<CommentResponse> ans = new ArrayList<>();
        for (Comment clone : result){
            if (clone.getIsActive()!=1) continue;
            ans.add(this.responseData(clone));
        }
        return ans;
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        List<Comment> temp = this.commentRepository.findByPost_PostId(postId);
        List<CommentResponse> result = new ArrayList<>();
        for (Comment clone: temp){
            if (clone.getIsActive()!=1) continue;
            result.add(
              this.responseData(clone)
            );
        }
        return result;
    }

    private Comment formData(CommentDTO commentDTO) throws DataNotFoundException {
        if (this.postService.findById(commentDTO.getPostId())==null){
            throw new DataNotFoundException("Can not comment on this available post");
        }
        if (userService.userExistsById(Long.parseLong(commentDTO.getAuthorId()))) {
            return Comment.builder()
                    .content(commentDTO.getContent())
                    .post(this.postService.findById(commentDTO.getPostId()))
                    .isActive(1L)
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
                .avatar(comment.getAuthor().getAvatar())
                .createdAt(comment.getCreatedAt())
                .authorName(comment.getAuthor().getUsername())
                .build();
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
