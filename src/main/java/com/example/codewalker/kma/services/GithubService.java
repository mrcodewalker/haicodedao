package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.GithubDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Facebook;
import com.example.codewalker.kma.models.Github;
import com.example.codewalker.kma.repositories.GithubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class GithubService implements IGithubService{
    private final GithubRepository githubRepository;
    @Override
    public Github createUser(GithubDTO githubDTO) {
        String email = "";
        if (!githubDTO.getEmail().contains("@gmail.com")){
            email = "";
        } else {
            email = githubDTO.getEmail();
        }
        if (githubRepository.findByEmail(githubDTO.getEmail()).isEmpty()
        || githubRepository.findByGithubId(githubDTO.getGithubId()) == null
        || githubRepository.findByUsername(githubDTO.getUsername()) == null ){
            Github github = Github.builder()
                    .email(githubDTO.getEmail())
                    .githubId(githubDTO.getGithubId())
                    .fullname(githubDTO.getFullname())
                    .avatarUrl(githubDTO.getAvatarUrl())
                    .username(githubDTO.getUsername())
                    .build();
            return githubRepository.save(github);
        }
        return null;
    }

    @Override
    public Github getGithubById(long id) throws DataNotFoundException {
        return this.githubRepository.findById(id).orElse(null);
    }

    @Override
    public List<Github> getAllGithub() {
        return this.githubRepository.findAll();
    }

    @Override
    public void deleteGithubById(long id) {
        this.githubRepository.deleteById(id);

    }

    @Override
    public Github getUserByEmail(String email) {
        return this.githubRepository.findUserByEmail(email);
    }
}
