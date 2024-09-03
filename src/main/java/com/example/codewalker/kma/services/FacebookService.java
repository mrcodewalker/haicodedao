package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.FacebookDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.Facebook;
import com.example.codewalker.kma.repositories.FacebookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacebookService implements IFacebookService {
    private final FacebookRepository facebookRepository;

    @Override
    public Facebook createUser(FacebookDTO facebookDTO) {
        if (facebookRepository.findByEmail(facebookDTO.getEmail()).isEmpty()){
            Facebook facebook = Facebook.builder()
                    .email(facebookDTO.getEmail())
                    .name(facebookDTO.getName())
                    .facebookId(facebookDTO.getFacebookId())
                    .build();
            return facebookRepository.save(facebook);
        }
        return null;
    }


    @Override
    public Facebook getUserById(long id) throws DataNotFoundException {
        return this.facebookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find email with id: "+id));
    }

    @Override
    public List<Facebook> getAllUsers() {
        return this.facebookRepository.findAll();
    }

    @Override
    public void deleteAccount(long id) {
        this.facebookRepository.deleteById(id);
    }

    @Override
    public Facebook getFacebookByEmail(String email) {
        return this.facebookRepository.findUserByEmail(email);
    }
}
