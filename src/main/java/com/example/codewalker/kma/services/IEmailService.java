package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.EmailDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Email;

import java.util.List;

public interface IEmailService {
    Email createUser(EmailDTO emailDTO);
    Email getUserById(long id) throws DataNotFoundException;
    List<Email> getAllUsers();
    void deleteCoupon(long id);
    Email getUserByEmail(String email);
}
