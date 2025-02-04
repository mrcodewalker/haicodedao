package com.example.codewalker.kma;

import io.jsonwebtoken.io.Encoders;

import java.io.File;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:4306/clone";
        String username = "root";
        String password = "123456789";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            System.out.println("Kết nối MySQL thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
