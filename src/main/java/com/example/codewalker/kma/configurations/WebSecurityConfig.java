package com.example.codewalker.kma.configurations;

import com.example.codewalker.kma.dtos.EmailDTO;
import com.example.codewalker.kma.dtos.FacebookDTO;
import com.example.codewalker.kma.dtos.GithubDTO;
import com.example.codewalker.kma.dtos.UserDTO;
import com.example.codewalker.kma.exceptions.DataNotFoundException;
import com.example.codewalker.kma.exceptions.InvalidParamException;
import com.example.codewalker.kma.filters.JwtTokenFilter;
import com.example.codewalker.kma.filters.JwtTokenProvider;
import com.example.codewalker.kma.models.Github;
import com.example.codewalker.kma.models.Role;
import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.UserRepository;
import com.example.codewalker.kma.responses.RegisterUserResponse;
import com.example.codewalker.kma.services.EmailService;
import com.example.codewalker.kma.services.FacebookService;
import com.example.codewalker.kma.services.GithubService;
import com.example.codewalker.kma.services.UserService;
import com.nimbusds.jose.Algorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private Random random = new Random();
    private static final String ALGORITHM = "AES";

    private final JwtTokenFilter jwtTokenFilter;
    private final EmailService emailService;
    private final FacebookService facebookService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubService githubService;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/login/oauth2/google", "/api/v1/login/oauth2/callback").permitAll()
                                .requestMatchers("/api/v1/calendar/**").permitAll()
                                .requestMatchers("/api/v1/crawl/**").permitAll()
                                .requestMatchers("/api/v1/login/**").permitAll()
                                .requestMatchers("/api/v1/ranking/**").permitAll()
                                .requestMatchers("/api/v1/schedules/**").permitAll()
                                .requestMatchers("/api/v1/scores/**").permitAll()
                                .requestMatchers("/api/v1/semester/**").permitAll()
                                .requestMatchers("/api/v1/students/**").permitAll()
                                .requestMatchers("/api/v1/subjects/**").permitAll()
                                .requestMatchers("/api/v1/users/**").permitAll()
                                .requestMatchers("/api/v1/posts/**").permitAll()
                                .requestMatchers("/api/v1/comments/**").permitAll()
                                .requestMatchers("/api/v1/likes/**").permitAll()
                                .anyRequest().authenticated()
                ) // Yêu cầu xác thực cho các yêu cầu khác
//                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không tạo session
//                );
        http.oauth2Login(oauth2 -> oauth2.successHandler(authenticationSuccessHandler()));
        http.cors(Customizer.withDefaults());
//                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Thêm filter tùy chỉnh của bạn
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Origin", "Authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect("http://localhost:4200/login?error=true");
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            Long id = 0L;
            String type = "";
            User temp = new User();
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauth2Token.getAuthorizedClientRegistrationId();

            if ("google".equals(clientRegistrationId)) {
                // Xử lý với Google (OidcUser)
                if (authentication.getPrincipal() instanceof OidcUser) {
                    OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                    String name = oidcUser.getFullName(); // Lấy tên người dùng
                    String email = oidcUser.getEmail(); // Lấy email người dùng
                    String picture = oidcUser.getPicture();
                    type = "google"; // Đặt loại cho Google


                    emailService.createUser(EmailDTO.builder()
                            .email(email)
                            .name(name)
                            .picture(picture)
                            .build());
                    String username = WebSecurityConfig.convertToUsername(name);
                    LocalDate today = LocalDate.now();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = today.format(formatter);
                    if (this.userRepository.findByEmail(email).isEmpty()) {
                        try {
                            do {
                                int number = random.nextInt(100);

                                username = username + number;

                                // Kiểm tra nếu username đã tồn tại trong cơ sở dữ liệu
                                if (this.userRepository.findByUsername(username).isEmpty()) {
                                    // Nếu không tồn tại, thoát khỏi vòng lặp
                                    break;
                                }
                            } while (true);
                            if (this.userRepository.findByUsername(username).isEmpty()) {
                                try {
                                    this.createUser(UserDTO.builder()
                                            .username(username)
                                            .providerName("Google")
                                            .email(email)
                                            .password(formattedDate)
                                            .build());
                                    temp = this.userRepository.findByUsername(username).get();
                                } catch (DataNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        User user = this.userRepository.findByEmail(email).get();
                        temp = user;
                    }
                } else {
                    // Nếu không phải OidcUser, xử lý lỗi hoặc thông báo
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected user type for Google");
                    return;
                }
            } else
                if ("facebook".equals(clientRegistrationId)) {
                // Xử lý với Facebook (OAuth2User)
                if (authentication.getPrincipal() instanceof OAuth2User) {
                    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                    String name = oauth2User.getAttribute("name"); // Lấy tên người dùng
                    String email = oauth2User.getAttribute("email"); // Lấy email người dùng
                    String facebookId = oauth2User.getAttribute("id");
                    type = "facebook"; // Đặt loại cho Facebook

                    facebookService.createUser(FacebookDTO.builder()
                            .facebookId(facebookId)
                            .email(email)
                            .name(name)
                            .build());

                    String username = WebSecurityConfig.convertToUsername(name);
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = today.format(formatter);
                    if (this.userRepository.findByEmail(email).isEmpty()) {
                        try {
                            do {
                                int number = random.nextInt(100);

                                username = username + number;

                                // Kiểm tra nếu username đã tồn tại trong cơ sở dữ liệu
                                if (this.userRepository.findByUsername(username).isEmpty()) {
                                    // Nếu không tồn tại, thoát khỏi vòng lặp
                                    break;
                                }
                            } while (true);
                            if (this.userRepository.findByUsername(username).isEmpty()) {
                                try {
                                    this.createUser(UserDTO.builder()
                                            .username(username)
                                            .providerName("Facebook")
                                            .email(email)
                                            .password(formattedDate)
                                            .build());
                                    temp = this.userRepository.findByUsername(username).get();
                                } catch (DataNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        User user = this.userRepository.findByEmail(email).get();
                        temp = user;
                    }
                } else {
                    // Nếu không phải OAuth2User, xử lý lỗi hoặc thông báo
                    response.sendError(HttpServletResponse .SC_INTERNAL_SERVER_ERROR, "Unexpected user type for Facebook");
                    return;
                }
            }
            if ("github".equals(clientRegistrationId)) {
                if (authentication.getPrincipal() instanceof OAuth2User) {
                    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                    String login = oauth2User.getAttribute("login"); // Lấy tên người dùng
                    String email = oauth2User.getAttribute("email");
                    Object idObject = oauth2User.getAttribute("id");
                    String idGithub = idObject.toString();
                    String avatarUrl = oauth2User.getAttribute("avatar_url");
                    String name = oauth2User.getAttribute("name"); // "Mr.CodeW"// "https://avatars.githubusercontent.com/u/136473311?v=4"

                    type = "github"; // Đặt loại cho Facebook
                    if (email == null || email.length() < 3) {
                        email = "";
                    }
                    Github github = githubService.createUser(GithubDTO.builder()
                            .avatarUrl(avatarUrl)
                            .githubId(idGithub)
                            .fullname(name)
                            .username(login)
                            .email(email)
                            .build());

                    String username = WebSecurityConfig.convertToUsername(login);
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = today.format(formatter);
                    if (email.contains("@gmail.com")) {
                        if (this.userRepository.findByEmail(email).isEmpty()) {
                            try {
                                do {
                                    int number = random.nextInt(100);

                                    username = username + number;

                                    // Kiểm tra nếu username đã tồn tại trong cơ sở dữ liệu
                                    if (this.userRepository.findByUsername(username).isEmpty()) {
                                        // Nếu không tồn tại, thoát khỏi vòng lặp
                                        break;
                                    }
                                } while (true);
                                if (this.userRepository.findByUsername(username).isEmpty()) {
                                    try {
                                        this.createUser(UserDTO.builder()
                                                .username(username)
                                                .providerName("Facebook")
                                                .email(email)
                                                .password(formattedDate)
                                                .build());
                                        temp = this.userRepository.findByUsername(username).get();
                                    } catch (DataNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            User user = this.userRepository.findByEmail(email).get();
                            temp = user;
                        }
                    } else {
                        Optional<User> optionalUser = userRepository.findByGithubId(idGithub);
                        if (optionalUser.isPresent()){
                            User user = optionalUser.get();
                            temp = user;
                        } else {
                            try {
                                do {
                                    int number = random.nextInt(100);

                                    username = username + number;

                                    if (this.userRepository.findByUsername(username).isEmpty()) {
                                            break;
                                        }
                                    } while (true);
                                    if (this.userRepository.findByUsername(username).isEmpty()) {
                                        try {
                                            this.createUser(UserDTO.builder()
                                                    .username(username)
                                                    .providerName("Github")
                                                            .githubId(idGithub)
                                                    .email(email)
                                                    .password(formattedDate)
                                                    .build());
                                            temp = this.userRepository.findByUsername(username).get();
                                        } catch (DataNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                        }
                    }
                }
            }
            // Thực hiện xử lý sau khi đăng nhập thành công, ví dụ: chuyển hướng
            String token = null;
            try {
                token = this.jwtTokenProvider.generateToken(temp);
            } catch (InvalidParamException e) {
                throw new RuntimeException(e);
            }
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = today.format(formatter);
            if (passwordEncoder.encode(formattedDate).equals(temp.getPassword())) {
                String redirectUrl = String.format("https://kma-legend.onrender.com/login/forum?username=%s&password=%s&token=%s&userId=%d&type=%s",
                        temp.getUsername(), formattedDate, URLEncoder.encode(token, StandardCharsets.UTF_8.toString()), temp.getUserId(), type);
                response.sendRedirect(redirectUrl);
            }
            else {
                String redirectUrl = String.format("https://kma-legend.onrender.com/login/forum?token=%s&userId=%d&type=%s",
                        URLEncoder.encode(token, StandardCharsets.UTF_8.toString()), temp.getUserId(), type);
                response.sendRedirect(redirectUrl);
            }
        };
    }
    public static String convertToUsername(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        // Chuyển thành chữ thường
        String lowerCaseName = name.toLowerCase();

        // Loại bỏ dấu và các ký tự không mong muốn
        String normalized = Normalizer.normalize(lowerCaseName, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}"); // Ký tự dấu
        String noDiacritics = pattern.matcher(normalized).replaceAll("");

        // Loại bỏ khoảng trắng
        String username = noDiacritics.replaceAll("\\s+", "");

        return username;
    }
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
        } else {
            githubId = user.getGithubId();
        }
        User clone =  User.builder()
                .email(email)
                .role(Role.builder()
                        .roleId(2L)
                        .roleName(Role.USER)
                        .build())
                .githubId(githubId)
                .username(user.getUsername())
                .providerName(user.getProviderName())
                .isActive(true)
                .build();
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        clone.setPassword(encodePassword);
        this.userRepository.save(
                clone
        );
        return RegisterUserResponse.builder()
                .userName(user.getUsername())
                .build();
    }
    public boolean userExists(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }
}
