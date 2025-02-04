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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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

    private String apiPrefix = "api/v1";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Ensure this is added
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/login/oauth2/google", "/api/v1/login/oauth2/callback").permitAll()
                                .requestMatchers("/api/v1/calendar/**").permitAll()
                                .requestMatchers("/api/v1/crawl/**").permitAll()
                                .requestMatchers("/api/v1/login/**").permitAll()
                                .requestMatchers("/api/v1/excel/upload").permitAll()
                                .requestMatchers("/api/v1/excel/toggle").permitAll()
                                .requestMatchers("/api/v1/excel/student").permitAll()
                                .requestMatchers("/api/v1/excel/export").permitAll()
                                .requestMatchers("/api/v1/excel/filter").permitAll()
                                .requestMatchers("/api/v1/excel/**").permitAll()
                                .requestMatchers("/api/v1/ranking/auto**").hasRole(Role.ADMIN)
                                .requestMatchers("/api/v1/ranking/update**").hasRole(Role.ADMIN)
                                .requestMatchers("/api/v1/ranking/query**").hasRole(Role.ADMIN)
                                .requestMatchers("/api/v1/ranking/**").permitAll()
                                .requestMatchers("/api/v1/schedules/**").permitAll()
                                .requestMatchers("/api/v1/scores/**").hasRole(Role.ADMIN)
                                .requestMatchers("/api/v1/semester/**").permitAll()
                                .requestMatchers("/api/v1/semester/refresh**").hasRole(Role.ADMIN)
                                .requestMatchers("/api/v1/students/**").permitAll()
                                .requestMatchers("/api/v1/subjects/**").permitAll()
                                .requestMatchers("/api/v1/users/**").permitAll()
                                .requestMatchers("/api/v1/posts/**").permitAll()
                                .requestMatchers("/api/v1/comments/**").permitAll()
                                .requestMatchers("/api/v1/likes/**").permitAll()
                                .requestMatchers("/api/v1/file/**").hasAnyRole(Role.ADMIN, Role.TEACHER)
                                .requestMatchers("/api/v1/graph**").hasAnyRole(Role.ADMIN, Role.TEACHER)
                                .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/**").permitAll()
                                .anyRequest().authenticated()
                ) // Yêu cầu xác thực cho các yêu cầu khác
//                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults());
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không tạo session
//                );
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
            response.sendRedirect("http://103.184.113.97:8080/login?error=true");
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
        if (user.getGithubId()==null){
            githubId = "";
        } else {
            githubId = user.getGithubId();
        }
        String avatar = "https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000";
        if (user.getAvatar()!=null){
            avatar = user.getAvatar();
        }
        User clone =  User.builder()
                .email(email)
                .avatar(avatar)
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
