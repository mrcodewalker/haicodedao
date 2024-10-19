package com.example.codewalker.kma.filters;

import com.example.codewalker.kma.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (this.isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String username = jwtTokenProvider.extractUserName(token);
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(username);
                if (jwtTokenProvider.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
    private boolean isByPassToken(@NonNull HttpServletRequest request){
        final List<Pair<String,String>> byPassTokens = Arrays.asList(
                Pair.of(String.format("%s/calendar", apiPrefix),"POST"),
                Pair.of(String.format("%s/calendar", apiPrefix),"GET"),
                Pair.of(String.format("%s/crawl", apiPrefix),"GET"),
                Pair.of(String.format("%s/crawl", apiPrefix),"POST"),
                Pair.of(String.format("%s/login", apiPrefix),"POST"),
                Pair.of(String.format("%s/login", apiPrefix),"GET"),
                Pair.of(String.format("%s/ranking", apiPrefix),"POST"),
                Pair.of(String.format("%s/ranking", apiPrefix),"GET"),
                Pair.of(String.format("%s/schedules", apiPrefix),"GET"),
                Pair.of(String.format("%s/schedules", apiPrefix),"POST"),
                Pair.of(String.format("%s/scores", apiPrefix),"GET"),
                Pair.of(String.format("%s/scores", apiPrefix),"POST"),
                Pair.of(String.format("%s/semester", apiPrefix),"GET"),
                Pair.of(String.format("%s/semester", apiPrefix),"POST"),
                Pair.of(String.format("%s/students", apiPrefix),"GET"),
                Pair.of(String.format("%s/students", apiPrefix),"POST"),
                Pair.of(String.format("%s/subjects", apiPrefix),"GET"),
                Pair.of(String.format("%s/subjects", apiPrefix),"POST"),
                Pair.of(String.format("%s/users/find", apiPrefix),"GET"),
                Pair.of(String.format("%s/users", apiPrefix),"OPTIONS"),
                Pair.of(String.format("%s/users/register", apiPrefix),"POST"),
                Pair.of(String.format("%s/users/login", apiPrefix),"POST"),
                Pair.of(String.format("%s/oauth2", apiPrefix),"GET"),
                Pair.of(String.format("%s/oauth2", apiPrefix),"POST"),
                Pair.of("login","GET"),
                Pair.of("login","POST")

        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        if (requestPath.equals(String.format("%s/orders", apiPrefix))
                && requestMethod.equals("GET")) {
            // Allow access to %s/orders
            return true;
        }
        if (requestPath.equals(String.format("%s/categories", apiPrefix))
                && requestMethod.equals("GET")) {
            // Allow access to %s/categories
            return true;
        }
        if (requestPath.equals(String.format("%s/products", apiPrefix))
                && requestMethod.equals("GET")) {
            // Allow access to %s/products
            return true;
        }
        if (requestPath.equals(String.format("%s/users/get-provinces", apiPrefix))
                && requestMethod.equals("GET")) {
            return true;
        }
        if (requestPath.contains(String.format("%s/vnpay/payment-callback", apiPrefix))
                && requestMethod.equals("GET")) {
            return true;
        }
        if (requestPath.contains(String.format("%s/users/get-districts/", apiPrefix))
                && requestMethod.equals("GET")) {
            return true;
        }
        if (requestPath.contains(String.format("%s/users/get-communes/", apiPrefix))
                && requestMethod.equals("GET")) {
            return true;
        }
        if (requestPath.contains(apiPrefix+"/products/images/")
                && requestMethod.equals("GET")) {
            // Allow access to %s/product_images
            return true;
        }

        for (Pair<String, String> bypassToken : byPassTokens) {
            if (requestPath.contains(bypassToken.getLeft())
                    && requestMethod.equals(bypassToken.getRight())) {
                return true;
            }
        }
        return false;
    }
}
