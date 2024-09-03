package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Facebook;
import com.example.codewalker.kma.models.Github;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GithubRepository extends JpaRepository<Github, Long> {
    List<Github> findByEmail(String email);
    Github findUserByEmail(String email);
    Github findByGithubId(String githubId);
    Github findByUsername(String username); // Sửa tên phương thức ở đây
}
