package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.PostRepository;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService{
    private final PostRepository postRepository;
    private final UserService userService;
    @Override
    public PostResponse createPost(PostDTO postDTO) throws DataNotFoundException {
        if (this.formData(postDTO)==null){
            throw new DataNotFoundException("Can not create a new post, please check again!");
        }
        User user = this.userService.getUserById(Long.parseLong(postDTO.getAuthorId()));
        return this.responseData(this.postRepository.save(Objects.requireNonNull(this.formData(postDTO))));
    }

    @Override
    public void deletePostById(Long id) {
        this.postRepository.deleteById(id);
    }

    @Override
    public Post findById(Long id) throws DataNotFoundException {
        return this.postRepository.findById(id).orElseThrow(()->new DataNotFoundException("Can not find post"));
    }

    @Override
    public PostResponse updatePost(PostDTO postDTO) {
        return null;
    }

    @Override
    public List<PostResponse> getPostByAuthorId(Long authorId) throws DataNotFoundException{
        List<Post> posts = this.postRepository.findByAuthor(
                this.userService.getUserById(authorId)
        );
        List<PostResponse> result = new ArrayList<>();
        for (Post clone: posts){
            result.add(
                    this.responseData(clone)
            );
        }
        return result;
    }
    private Post formData(PostDTO postDTO) throws DataNotFoundException{
        User user = this.userService.getUserById(Long.parseLong(postDTO.getAuthorId()));
            return Post.builder()
                    .imageUrl(postDTO.getImageUrl())
                    .content(postDTO.getContent())
                    .author(this.userService.getUserById(Long.parseLong(postDTO.getAuthorId())))
                    .build();
    }
    private PostResponse responseData(Post post){
        return PostResponse.builder()
                .postId(post.getPostId())
                .createdAt(post.getCreatedAt())
                .authorName(post.getAuthor().getUsername())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .build();
    }
}
