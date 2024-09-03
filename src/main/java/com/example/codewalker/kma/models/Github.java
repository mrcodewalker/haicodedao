package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "github")
public class Github {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(length = 255)
    private String fullname;

    @Column(name = "github_id")
    private String githubId;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "email")
    private String email;
}
