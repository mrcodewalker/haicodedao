package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.CommentDTO;
import com.example.codewalker.kma.dtos.LikeDTO;
import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Comment;
import com.example.codewalker.kma.models.Like;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.LikeRepository;
import com.example.codewalker.kma.repositories.PostRepository;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.CommentResponse;
import com.example.codewalker.kma.responses.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeService implements ILikeService{
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;
    @Override
    public LikeResponse createLike(LikeDTO likeDTO) throws DataNotFoundException {
        Post post = this.postService.findById((likeDTO.getPostId()));
        List<Like> list = this.likeRepository.findByPost(post);
        User user = this.userService.getUserById(Long.parseLong(likeDTO.getAuthorId()));
        for (Like like: list){
            if (like.getAuthor().getUsername().equalsIgnoreCase(user.getUsername())
            && like.getPost().getPostId().equals(likeDTO.getPostId())){
                this.deleteLikeById(like.getLikeId());
                return null;
            }
        }
        if (this.formData(likeDTO)==null){
            throw new DataNotFoundException("Can not like");
        }
        return this.responseData(
                this.likeRepository.save(Objects.requireNonNull(this.formData(likeDTO)))
        );
    }

    @Override
    public void deleteLikeById(Long id) {
        this.likeRepository.deleteById(id);
    }

    @Override
    public Like findById(Long id) throws DataNotFoundException{
        return this.likeRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Can not find like by id")
        );
    }

    @Override
    public LikeResponse updateLike(PostDTO postDTO) {
        return null;
    }

    @Override
    public List<LikeResponse> getListLikeByPostId(Long postId) throws DataNotFoundException {
        List<Like> list = this.likeRepository.findByPost(
                this.postService.findById(postId)
        );
        List<LikeResponse> answer = new ArrayList<>();
        for (Like clone : list){
            answer.add(
                    this.responseData(clone)
            );
        }
        return answer;
    }
    private Like formData(LikeDTO likeDTO) throws DataNotFoundException {
        if (this.postService.findById(likeDTO.getPostId())==null){
            throw new DataNotFoundException("Can not comment on this available post");
        }
        if (userService.userExistsById(Long.parseLong(likeDTO.getAuthorId()))) {
            return Like.builder()
                    .post(this.postService.findById(likeDTO.getPostId()))
                    .author(this.userService.getUserById(Long.parseLong(likeDTO.getAuthorId())))
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        return null;
    }
    private LikeResponse responseData(Like like){
        return LikeResponse.builder()
                .likeId(like.getLikeId())
                .authorName(like.getAuthor().getUsername())
                .postId(like.getPost().getPostId())
                .build();
    }
}
