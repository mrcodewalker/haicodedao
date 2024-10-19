package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User user);
    Page<Post> findAll(Pageable pageable);
    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.postId = :postId")
    Optional<Post> findPostWithAuthorById(@Param("postId") Long postId);
    Page<Post> findAllByIsActive(int isActive, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.author")
    List<Post> findAllPostsWithAuthors();
    @Query("SELECT p FROM Post p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND p.isActive = 1")
    Page<Post> searchPostsByTitleOrContent(@Param("searchTerm") String searchTerm, Pageable pageable);
    // Trong PostRepository:
    @Query("SELECT p FROM Post p WHERE p.isActive=1 AND p.createdAt >= :startDate ORDER BY p.views DESC")
    Page<Post> findPopularThisWeek(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    // Trong PostRepository:
    @Query("SELECT p FROM Post p WHERE p.isActive = 1 ORDER BY p.views DESC")
    Page<Post> findPopularAllTime(Pageable pageable);
}
