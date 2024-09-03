package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Comment;
import com.example.codewalker.kma.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor(User user);
}
