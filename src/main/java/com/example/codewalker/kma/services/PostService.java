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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService{
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

        // Lấy danh sách Post từ repository với điều kiện isActive = 1
        Page<Post> postPage = postRepository.findAllByIsActive(1, pageable);

        // Chuyển đổi Page<Post> thành List<PostResponse>
        List<PostResponse> list = new ArrayList<>();
        Set<Long> postIds = new HashSet<>(); // Dùng để theo dõi các postId đã thêm vào list

        while (list.size() < 7) {
            if (postPage.isEmpty()) {
                break; // Không còn bản ghi nào nữa
            }

            for (Post post : postPage.getContent()) {
                if (!postIds.contains(post.getPostId())) { // Kiểm tra xem postId đã có trong list chưa
                    post.setImageUrl(post.getAuthor().getAvatar());
                    post.setViews(post.getViews());
                    list.add(this.responseData(post));
                    postIds.add(post.getPostId()); // Đánh dấu postId đã thêm

                    // Nếu đã đủ 7 bản ghi, không cần thêm nữa
                    if (list.size() >= 7) {
                        break;
                    }
                }
            }

            // Tăng chỉ số trang để lấy dữ liệu của trang tiếp theo trong lần lặp tiếp theo
            pageable = pageable.next();
            postPage = postRepository.findAllByIsActive(1, pageable); // Lấy lại postPage của trang mới
        }


        // Tính toán tổng số trang chỉ cho các bài đang hoạt động (isActive = 1)
        int totalPages = postPage.getTotalPages();  // Tổng số trang chỉ dựa trên các bài đang hoạt động

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page + "")
                .size(list.size() + "")
                .totalPages(totalPages + "")
                .build();
    }

    @Override
    public CollectPostDataResponse getAllPostDecrease(int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Post> postPage = postRepository.findAllByIsActive(1, pageable);

        List<PostResponse> list = new ArrayList<>();
        Set<Long> postIds = new HashSet<>(); // Dùng để theo dõi các postId đã thêm vào list

        while (list.size() < 7) {
            if (postPage.isEmpty()) {
                break; // Không còn bản ghi nào nữa
            }

            for (Post post : postPage.getContent()) {
                if (!postIds.contains(post.getPostId())) { // Kiểm tra xem postId đã có trong list chưa
                    post.setImageUrl(post.getAuthor().getAvatar());
                    post.setViews(post.getViews());
                    list.add(this.responseData(post));
                    postIds.add(post.getPostId()); // Đánh dấu postId đã thêm

                    // Nếu đã đủ 7 bản ghi, không cần thêm nữa
                    if (list.size() >= 7) {
                        break;
                    }
                }
            }

            // Tăng chỉ số trang để lấy dữ liệu của trang tiếp theo trong lần lặp tiếp theo
            pageable = pageable.next();
            postPage = postRepository.findAllByIsActive(1, pageable); // Lấy lại postPage của trang mới
        }

        int totalPages = postPage.getTotalPages(); // Tổng số trang cho bài đang hoạt động

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page + "")
                .size(list.size() + "")
                .totalPages(totalPages + "")
                .build();
    }


    @Override
    public CollectPostDataResponse getAllPostPopular(int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postRepository.findAllByIsActive(1, pageable);

        List<PostResponse> list = new ArrayList<>();
        Set<Long> postIds = new HashSet<>(); // Dùng để theo dõi các postId đã thêm vào list

        while (list.size() < 7) {
            if (postPage.isEmpty()) {
                break; // Không còn bản ghi nào nữa
            }

            for (Post post : postPage.getContent()) {
                if (!postIds.contains(post.getPostId())) { // Kiểm tra xem postId đã có trong list chưa
                    post.setImageUrl(post.getAuthor().getAvatar());
                    post.setViews(post.getViews());
                    list.add(this.responseData(post));
                    postIds.add(post.getPostId()); // Đánh dấu postId đã thêm

                    // Nếu đã đủ 7 bản ghi, không cần thêm nữa
                    if (list.size() >= 7) {
                        break;
                    }
                }
            }

            // Tăng chỉ số trang để lấy dữ liệu của trang tiếp theo trong lần lặp tiếp theo
            pageable = pageable.next();
            postPage = postRepository.findAllByIsActive(1, pageable); // Lấy lại postPage của trang mới
        }

        // Sắp xếp theo số lượng replies
        list.sort((p1, p2) -> Integer.parseInt(p2.getReplies()) - Integer.parseInt(p1.getReplies()));

        int totalPages = postPage.getTotalPages();

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page + "")
                .size(list.size() + "")
                .totalPages(totalPages + "")
                .build();
    }


    @Override
    public CollectPostDataResponse searchPosts(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postRepository.searchPostsByTitleOrContent(searchTerm, pageable);

        List<PostResponse> list = new ArrayList<>();
        for (Post post : postPage.getContent()) {
            post.setImageUrl(post.getAuthor().getAvatar());
            post.setViews(post.getViews());
            list.add(this.responseData(post));
        }

        int totalPages = postPage.getTotalPages();

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page + "")
                .size(list.size() + "")
                .totalPages(totalPages + "")
                .build();
    }



    @Override
    public CollectPostDataResponse popularThisWeek(int page) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> postPage = postRepository.findPopularThisWeek(oneWeekAgo, pageable);

        List<PostResponse> list = new ArrayList<>();
        Set<Long> postIds = new HashSet<>(); // Dùng để theo dõi các postId đã thêm vào list

        while (list.size() < 7) {
            if (postPage.isEmpty()) {
                break; // Không còn bản ghi nào nữa
            }

            for (Post post : postPage.getContent()) {
                if (!postIds.contains(post.getPostId())) { // Kiểm tra xem postId đã có trong list chưa
                    post.setImageUrl(post.getAuthor().getAvatar());
                    post.setViews(post.getViews());
                    list.add(this.responseData(post));
                    postIds.add(post.getPostId()); // Đánh dấu postId đã thêm

                    // Nếu đã đủ 7 bản ghi, không cần thêm nữa
                    if (list.size() >= 7) {
                        break;
                    }
                }
            }

            // Tăng chỉ số trang để lấy dữ liệu của trang tiếp theo trong lần lặp tiếp theo
            pageable = pageable.next();
            postPage = postRepository.findAllByIsActive(1, pageable); // Lấy lại postPage của trang mới
        }

        int totalPages = postPage.getTotalPages();

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page + "")
                .size(list.size() + "")
                .totalPages(totalPages + "")
                .build();
    }


    @Override
    public CollectPostDataResponse popularAllTimes(int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> postPage = postRepository.findPopularAllTime(pageable);

        List<PostResponse> list = new ArrayList<>();
        Set<Long> postIds = new HashSet<>(); // Dùng để theo dõi các postId đã thêm vào list

        while (list.size() < 7) {
            if (postPage.isEmpty()) {
                break; // Không còn bản ghi nào nữa
            }

            for (Post post : postPage.getContent()) {
                if (!postIds.contains(post.getPostId())) { // Kiểm tra xem postId đã có trong list chưa
                    post.setImageUrl(post.getAuthor().getAvatar());
                    post.setViews(post.getViews());
                    list.add(this.responseData(post));
                    postIds.add(post.getPostId()); // Đánh dấu postId đã thêm

                    // Nếu đã đủ 7 bản ghi, không cần thêm nữa
                    if (list.size() >= 7) {
                        break;
                    }
                }
            }

            // Tăng chỉ số trang để lấy dữ liệu của trang tiếp theo trong lần lặp tiếp theo
            pageable = pageable.next();
            postPage = postRepository.findAllByIsActive(1, pageable); // Lấy lại postPage của trang mới
        }

        int totalPages = postPage.getTotalPages();

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page + "")
                .size(list.size() + "")
                .totalPages(totalPages + "")
                .build();
    }

    @Override
    public CollectPostDataResponse noRepliesYet(int page) {
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> postPage = postRepository.findAllByIsActive(1, pageable);
        int count = 0;
        List<PostResponse> list = new ArrayList<>();
        Set<Long> postIds = new HashSet<>(); // Dùng để theo dõi các postId đã thêm vào list

        while (list.size() < 7) {
            if (postPage.isEmpty()) {
                break; // Không còn bản ghi nào nữa
            }

            for (Post post : postPage.getContent()) {
                if (!postIds.contains(post.getPostId())
                && this.getCommentsByPostId(post.getPostId()).isEmpty()) { // Kiểm tra xem postId đã có trong list chưa
                    post.setImageUrl(post.getAuthor().getAvatar());
                    post.setViews(post.getViews());
                    list.add(this.responseData(post));
                    postIds.add(post.getPostId()); // Đánh dấu postId đã thêm

                    // Nếu đã đủ 7 bản ghi, không cần thêm nữa
                    if (list.size() >= 7) {
                        break;
                    }
                }
            }

            // Tăng chỉ số trang để lấy dữ liệu của trang tiếp theo trong lần lặp tiếp theo
            pageable = pageable.next();
            postPage = postRepository.findAllByIsActive(1, pageable); // Lấy lại postPage của trang mới
        }
        int totalPages = postPage.getTotalPages();

        return CollectPostDataResponse.builder()
                .postResponse(list)
                .page(page + "")
                .size(list.size() + "")
                .totalPages(totalPages + "")
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
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
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
        String jwtSecret = "Z54uiPhveohL/uORp8a8rHhu0qalR4Mj+aIOz5ZA5zY=";
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
