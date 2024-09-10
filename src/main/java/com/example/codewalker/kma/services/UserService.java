package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.UserDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.exceptions.InvalidParamException;
import com.example.codewalker.kma.filters.JwtTokenProvider;
import com.example.codewalker.kma.models.Role;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.ProfileResponse;
import com.example.codewalker.kma.responses.RegisterUserResponse;
import com.example.codewalker.kma.responses.StatusResponse;
import com.example.codewalker.kma.responses.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    @Value("${jwt.secret}")
    private String jwtSecret;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    @Transactional
    public RegisterUserResponse createUser(UserDTO user) throws Exception{
        if (user.getUsername().length()<6
                || user.getPassword().length()<8){
            throw new InvalidParamException("Please check your information again!");
        }
        if (this.userExists(user.getUsername())){
            throw new DataNotFoundException("User name is already exists");
        }
        String email = "";
        if (user.getEmail().contains("@gmail.com")){
            email = user.getEmail();
        }
        String githubId = "";
        if (user.getGithubId().length()<3){
            githubId = "";
        }
        String avatar = "https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000";
        if (user.getAvatar().length()>3){
           avatar = user.getAvatar();
        }
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
        User existingUser = this.findUserByUserName(username);
        if (existingUser == null) {
            return TokenResponse.builder()
                    .status("403")
                    .username(username)
                    .build();
        }

        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            return TokenResponse.builder()
                    .status("403")
                    .username(username)
                    .build();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password,
                existingUser.getAuthorities()
        );

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            // Logging the exception to see more details
//            e.printStackTrace();
            return TokenResponse.builder()
                    .status("403")
                    .username(username)
                    .build();
        }

        return TokenResponse.builder()
                .token(jwtTokenProvider.generateToken(existingUser))
                .id(existingUser.getUserId()+"")
                .status("200")
                .username(username)
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
        String extractToken = this.getUsernameFromToken(token.substring(7));
        if (this.userExistsById(Long.parseLong(id))){
            User user = this.getUserById(Long.parseLong(id));
            if (!user.getUsername().equalsIgnoreCase(extractToken)){
                return null;
            }
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

    private String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
