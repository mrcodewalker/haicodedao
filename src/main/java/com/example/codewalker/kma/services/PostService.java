package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.PostDTO;
import com.example.codewalker.kma.dtos.UpdateDataDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.filters.JwtTokenProvider;
import com.example.codewalker.kma.models.Comment;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.CommentRepository;
import com.example.codewalker.kma.repositories.PostRepository;
import com.example.codewalker.kma.responses.CollectPostDataResponse;
import com.example.codewalker.kma.responses.CommentResponse;
import com.example.codewalker.kma.responses.PostResponse;
import com.example.codewalker.kma.responses.StatusResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService{
    @Value("${jwt.secret}")
    private String jwtSecret;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EntityManager entityManager;

    @Override
    public PostResponse createPost(PostDTO postDTO) throws DataNotFoundException {
        if (this.formData(postDTO)==null){
            throw new DataNotFoundException("Can not create a new post, please check again!");
        }
        User user = this.userService.getUserById(Long.parseLong(postDTO.getAuthorId()));
        return this.responseData(this.postRepository.save(Objects.requireNonNull(this.formData(postDTO))));
    }

    @Override
    public StatusResponse deletePostById(Long id, String token) throws DataNotFoundException {
        Optional<Post> post = this.postRepository.findPostWithAuthorById(id);
        if (post.isEmpty()){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        Post temp = post.get();
        if (!this.getUsernameFromToken(token.substring(7))
                .equalsIgnoreCase(temp.getAuthor().getUsername())
        ){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        temp.setIsActive(0L);
        this.postRepository.save(temp);
        return StatusResponse.builder()
                .status("200")
                .build();
    }

    @Override
    public Post findById(Long id) throws DataNotFoundException {
        return this.postRepository.findById(id).orElseThrow(()->new DataNotFoundException("Can not find post"));
    }

    @Override
    @Transactional
    public StatusResponse updatePost(UpdateDataDTO postDTO
                , String token) throws DataNotFoundException {
        Optional<Post> post = this.postRepository.findPostWithAuthorById(postDTO.getId());
        if (post.isEmpty()){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        Post temp = post.get();
        if (!this.getUsernameFromToken(token.substring(7))
                .equalsIgnoreCase(temp.getAuthor().getUsername())
        ){
            return StatusResponse.builder()
                    .status("403")
                    .build();
        }
        temp.setContent(postDTO.getContent());
        this.postRepository.save(temp);
        this.entityManager.flush();
        return StatusResponse.builder()
                .status("200")
                .build();
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

    @Override
    public CollectPostDataResponse getAllPosts(int page) {
        // Tạo Pageable với phân trang (7 bản ghi mỗi trang) và sắp xếp theo createdAt giảm dần
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Lấy Page<Post> từ repository
        Page<Post> postPage = postRepository.findAll(pageable);

        // Chuyển đổi Page<Post> thành List<PostResponse>
        List<PostResponse> list = new ArrayList<>();
        for (Post post : postPage.getContent()) {
            if (post.getIsActive()!=1) continue;
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page+"")
                .size(list.size()+"")
                .totalPages(list.size()/7+1+"")
                .build();
    }

    @Override
    public CollectPostDataResponse getAllPostDecrease(int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.ASC, "createdAt"));

        // Lấy Page<Post> từ repository
        Page<Post> postPage = postRepository.findAll(pageable);

        // Chuyển đổi Page<Post> thành List<PostResponse>
        List<PostResponse> list = new ArrayList<>();
        for (Post post : postPage.getContent()) {
            if (post.getIsActive()!=1) continue;
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page+"")
                .size(list.size()+"")
                .totalPages(list.size()/7+1+"")
                .build();
    }

    @Override
    public CollectPostDataResponse getAllPostPopular(int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Lấy Page<Post> từ repository
        Page<Post> postPage = postRepository.findAll(pageable);

        // Chuyển đổi Page<Post> thành List<PostResponse>
        List<PostResponse> list = new ArrayList<>();
        for (Post post : postPage.getContent()) {
            if (post.getIsActive()!=1) continue;
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }
        list.sort((p1, p2) -> {
            // So sánh số lượng replies của hai PostResponse
            Integer replies1 = Integer.parseInt(p1.getReplies());
            Integer replies2 = Integer.parseInt(p2.getReplies());
            return replies2.compareTo(replies1);  // Sắp xếp từ cao xuống thấp
        });
        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page+"")
                .size(list.size()+"")
                .totalPages(list.size()/7+1+"")
                .build();
    }
    @Override
    public CollectPostDataResponse searchPosts(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> posts = postRepository.searchPostsByTitleOrContent(searchTerm, pageable);
        List<PostResponse> list = new ArrayList<>();
        for (Post post : posts.getContent()) {
            if (post.getIsActive()!=1) continue;
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }
        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page+"")
                .size(list.size()+"")
                .totalPages(list.size()/7+1+"")
                .build();
    }

    @Override
    public CollectPostDataResponse popularThisWeek(int page) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> temp = postRepository.findPopularThisWeek(oneWeekAgo, pageable);

        List<PostResponse> list = new ArrayList<>();
        for (Post post : temp) {
            if (post.getIsActive()!=1) continue;
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page("")
                .size(list.size()+"")
                .totalPages(list.size()/7+1+"")
                .build();
    }

    @Override
    public CollectPostDataResponse popularAllTimes(int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> temp = postRepository.findPopularAllTime(pageable);

        List<PostResponse> list = new ArrayList<>();
        for (Post post : temp) {
            if (post.getIsActive()!=1) continue;
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page("")
                .size(list.size()+"")
                .totalPages(list.size()/7+1+"")
                .build();
    }

    @Override
    public CollectPostDataResponse noRepliesYet(int page) {
        // Tạo Pageable với phân trang (7 bản ghi mỗi trang) và sắp xếp theo createdAt giảm dần
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Lấy Page<Post> từ repository
        Page<Post> postPage = postRepository.findAll(pageable);

        // Chuyển đổi Page<Post> thành List<PostResponse>
        List<PostResponse> list = new ArrayList<>();
        for (Post post : postPage.getContent()) {
            if (post.getIsActive()!=1) continue;
            if (this.getCommentsByPostId(post.getPostId()).size()!=0) continue;
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page+"")
                .size(list.size()+"")
                .totalPages(list.size()/7+1+"")
                .build();
    }

    @Override
    public void increaseView(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isEmpty()) return;
        Post temp = post.get();
        temp.setViews(temp.getViews()+1);
        this.postRepository.save(temp);
    }


    private Post formData(PostDTO postDTO) throws DataNotFoundException{
        User user = this.userService.getUserById(Long.parseLong(postDTO.getAuthorId()));
            return Post.builder()
                    .imageUrl(user.getAvatar())
                    .content(postDTO.getContent())
                    .title(postDTO.getTitle())
                    .views(1L)
                    .isActive(1L)
                    .author(this.userService.getUserById(Long.parseLong(postDTO.getAuthorId())))
                    .build();
    }
    private PostResponse responseData(Post post){
        return PostResponse.builder()
                .title(post.getTitle())
                .postId(post.getPostId())
                .views(post.getViews())
                .replies(this.getCommentsByPostId(post.getPostId()).size()+"")
                .createdAt(post.getCreatedAt())
                .authorName(post.getAuthor().getUsername())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .build();
    }
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
    private CommentResponse responseData(Comment comment){
        return CommentResponse.builder()
                .commentId(comment.getCommentId()+"")
                .postId(comment.getPost().getPostId()+"")
                .content(comment.getContent())
                .authorName(comment.getAuthor().getUsername())
                .build();
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
