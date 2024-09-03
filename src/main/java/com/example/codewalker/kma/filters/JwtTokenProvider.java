package com.example.codewalker.kma.filters;

import com.example.codewalker.kma.exceptions.InvalidParamException;
import com.example.codewalker.kma.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Tạo token
    public String generateToken(User user) throws InvalidParamException {
        Map<String, Object> claims = new HashMap<>();
//        this.generateSecretKey();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getUserId());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        try{
            String token = Jwts.builder()
                    .setClaims(claims) // how to extract claims from this ?
                    .setIssuedAt(now) // Thêm thời gian phát hành
                    .setSubject(user.getUsername())
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS256, getSigninKey()) // Sử dụng phương thức signWith mới
                    .compact();
            return token;
        } catch (Exception e){
            throw new InvalidParamException("Can not create jwt token, error: "+e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Lấy thông tin từ token
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    // Lấy tên người dùng từ token
    public String extractUserName(String token){
        return this.extractClaim(token, Claims::getSubject);
    }

    private Key getSigninKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigninKey())
                .parseClaimsJws(token)
                .getBody();
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());

    }
}
