package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findByEmail(String email);
    Email findUserByEmail(String email);
}
