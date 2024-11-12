package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.UserDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.exceptions.InvalidParamException;
import com.example.codewalker.kma.filters.JwtTokenProvider;
import com.example.codewalker.kma.models.Role;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private String jwtSecret = "Z54uiPhveohL/uORp8a8rHhu0qalR4Mj+aIOz5ZA5zY=";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
//    @PostConstruct
//    public void testRepository() {
//        System.out.println(userRepository.findByUsername("haicodedao"));
//    }
    @Override
    @Transactional
    public RegisterUserResponse createUser(UserDTO user) throws Exception{
        if (user.getUsername().length()<6
                || user.getPassword().length()<8){
            return RegisterUserResponse.builder()
                    .userName(null)
                    .build();
        }
        if (this.userExists(user.getUsername())){
            throw new DataNotFoundException("User name is already exists");
        }
        String email = "";
        if (user.getEmail().contains("@gmail.com")){
            email = user.getEmail();
        } else {
            return RegisterUserResponse.builder()
                    .userName(null)
                    .build();
        }
        String githubId = "";
        String avatar = "https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000";
        User clone =  User.builder()
                .email(email)
                .role(Role.builder()
                        .roleId(2L)
                        .roleName(Role.USER)
                        .build())
                .avatar(avatar)
                .githubId(githubId)
                .username(user.getUsername())
                .isActive(true)
                .build();
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        clone.setPassword(encodePassword);
        if (user.getProviderName()==null||user.getProviderName().length()<2){
            clone.setProviderName("KMA Legend");
        }
        this.userRepository.save(
               clone
        );
        return RegisterUserResponse.builder()
                .userName(user.getUsername())
                .build();
    }

    @Override
    public TokenResponse login(String username, String password) throws Exception {
        Optional<User> existingUser = this.userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            return TokenResponse.builder()
                    .status("403")
                    .build();
        }
        User user = existingUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return TokenResponse.builder()
                    .status("403")
                    .build();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password,
                user.getAuthorities()
        );

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            // Logging the exception to see more details
//            e.printStackTrace();
            return TokenResponse.builder()
                    .status("403")
                    .build();
        }

        return TokenResponse.builder()
                .token(jwtTokenProvider.generateToken(user))
                .status("200")
                .role(user.getRole().getRoleName())
                .username(user.getUsername())
                .id(user.getUserId()+"")
                .build();
    }


    @Override
    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User findUserByUserName(String username) {
        if (this.getUserByUsername(username).isPresent()) {
            return this.getUserByUsername(username).get();
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public User getUserById(Long id) throws DataNotFoundException {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find user by id"));
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public boolean userExists(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean userExistsById(Long id) {
        return this.userRepository.findById(id).isPresent();
    }
    public RegisterUserResponse checkUserName(String username){
        if (this.findUserByUserName(username)!=null){
            return RegisterUserResponse.builder()
                    .userName(this.findUserByUserName(username).getUsername())
                    .build();
        }
        return RegisterUserResponse.builder()
                .userName(null)
                .build();
    }
    @Override
    public User getUserByEmail(String email) throws DataNotFoundException {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public ProfileResponse getUserProfile(String id, String token) throws DataNotFoundException {
        if (jwtTokenProvider.extractUserName(token.substring(7))
                .equalsIgnoreCase(this.getUserById(Long.parseLong(id)).getUsername())){
            User user = this.getUserById(Long.parseLong(id));
            return ProfileResponse.builder()
                    .avatar(user.getAvatar())
                    .providerName(user.getProviderName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();
        }
        return null;
    }

    @Override
    public StatusResponse updateAvatar(String id, String avatar, String token) throws DataNotFoundException {
        String extractToken = this.getUsernameFromToken(token.substring(7));
        if (this.userExistsById(Long.parseLong(id))) {
            User user = this.getUserById(Long.parseLong(id));
            if (!user.getUsername().equalsIgnoreCase(extractToken)) {
                return StatusResponse.builder()
                        .status("403")
                        .build();
            } else {
                User temp = this.getUserById(Long.parseLong(id));
                if (avatar.length()<3){
                    temp.setAvatar("https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000");
                    this.updateUser(temp);
                } else {
                    temp.setAvatar(avatar);
                }
                this.updateUser(temp);
                return StatusResponse.builder()
                        .status("200")
                        .build();
            }
        }
        return StatusResponse.builder()
                .status("403")
                .build();
    }

    @Override
    public List<CollectUsersResponse> collectUsers() {
        List<User> list = this.userRepository.findAll();
        List<CollectUsersResponse> result = new ArrayList<>();
        for(User clone: list){
            result.add(
                    CollectUsersResponse.builder()
                            .id(clone.getUserId())
                            .avatar(clone.getAvatar())
                            .email(clone.getEmail())
                            .githubId(clone.getGithubId())
                            .roleName(clone.getRole().getRoleName())
                            .username(clone.getUsername())
                            .providerName(clone.getProviderName())
                            .isActive(clone.isActive())
                            .build()
            );
        }
        return result;
    }

    @Override
    public StatusResponse existByEmailOrUserName(UserDTO userDTO) {
        List<User> user = this.userRepository.findExistUser(userDTO.getUsername(), userDTO.getEmail());
        return (user.size()>0)
                ? StatusResponse.builder()
                    .status("666")
                    .build()
                : StatusResponse.builder()
                    .status("200")
                    .build();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
