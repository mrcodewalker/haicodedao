package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.EmailDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Email;
import com.example.codewalker.kma.repositories.EmailRepository;
import com.example.codewalker.kma.services.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final EmailRepository emailRepository;
    @Override
    public Email createUser(EmailDTO emailDTO){
        if (emailRepository.findByEmail(emailDTO.getEmail()).isEmpty()){
            Email email = Email.builder()
                    .email(emailDTO.getEmail())
                    .name(emailDTO.getName())
                    .picture(emailDTO.getPicture())
                    .build();
            return emailRepository.save(email);
        }
        return null;
    }

    @Override
    public Email getUserById(long id) throws DataNotFoundException {
        return this.emailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find email with id: "+id));
    }

    @Override
    public List<Email> getAllUsers() {
        return this.emailRepository.findAll();
    }

    @Override
    public void deleteCoupon(long id) {

    }

    @Override
    public Email getUserByEmail(String email) {
        return this.emailRepository.findUserByEmail(email);
    }
}
