package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByGithubId(String githubId);
}
