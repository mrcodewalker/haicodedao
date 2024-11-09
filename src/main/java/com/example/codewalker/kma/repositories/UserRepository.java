package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByGithubId(String githubId);
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    List<User> findExistUser(@Param("username") String username, @Param("email") String email);
}
