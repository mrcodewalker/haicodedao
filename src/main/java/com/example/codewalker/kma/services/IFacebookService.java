package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.FacebookDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Facebook;

import java.util.List;

public interface IFacebookService {
    Facebook createUser(FacebookDTO facebookDTO);
    Facebook getUserById(long id) throws DataNotFoundException;
    List<Facebook> getAllUsers();
    void deleteAccount(long id);
    Facebook getFacebookByEmail(String email);
}