package com.example.codewalker.kma;

import io.jsonwebtoken.io.Encoders;

import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(new Main().generateSecretKey());
    }
    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
}
