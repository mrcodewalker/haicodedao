package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Comment;
import com.example.codewalker.kma.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor(User user);
    List<Comment> findByPost_PostId(Long postId);
    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.post.postId = :postId")
    List<Comment> findCommentsByPostIdWithAuthor(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.commentId = :commentId")
    Comment findCommentByIdWithAuthor(@Param("commentId") Long commentId);
}
