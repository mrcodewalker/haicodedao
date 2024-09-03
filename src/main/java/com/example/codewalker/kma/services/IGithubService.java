package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.EmailDTO;
import com.example.codewalker.kma.dtos.GithubDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Email;
import com.example.codewalker.kma.models.Github;

import java.util.List;

public interface IGithubService {
    Github createUser(GithubDTO githubDTO);
    Github getGithubById(long id) throws DataNotFoundException;
    List<Github> getAllGithub();
    void deleteGithubById(long id);
    Github getUserByEmail(String email);
}
