package com.example.codewalker.kma;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

public class GenerateSecretKey {
    public static void main(String[] args) throws Exception {
        SecretKey secretKey = GenerateSecretKey.generateKey();
        System.out.println("Secret Key: " + secretKey);
    }
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // Sử dụng độ dài khóa 128 bit
        return keyGenerator.generateKey();
    }
}
