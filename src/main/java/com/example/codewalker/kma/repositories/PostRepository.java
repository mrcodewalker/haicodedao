package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Like;
import com.example.codewalker.kma.models.Post;
import com.example.codewalker.kma.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User user);
}
