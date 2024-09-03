package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Like;
import com.example.codewalker.kma.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPost(Post post);
}
