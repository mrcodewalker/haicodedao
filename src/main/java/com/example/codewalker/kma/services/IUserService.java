package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.UserDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.responses.*;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    RegisterUserResponse createUser(UserDTO user) throws Exception;

    TokenResponse login(String username, String password) throws Exception;

    User updateUser(User user);

    User findUserByUserName(String username);

    void deleteUser(Long id);

    User getUserById(Long id) throws DataNotFoundException;

    List<User> getAllUsers();

    Optional<User> getUserByUsername(String username);

    boolean userExists(String username);

    boolean userExistsById(Long id);

    User getUserByEmail(String email) throws DataNotFoundException;
    ProfileResponse getUserProfile(String id, String token) throws DataNotFoundException;

    StatusResponse updateAvatar(String id, String avatar, String token) throws DataNotFoundException;
    List<CollectUsersResponse> collectUsers();
    StatusResponse existByEmailOrUserName(UserDTO userDTO);
}