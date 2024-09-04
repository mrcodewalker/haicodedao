package com.example.codewalker.kma;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// Tải biến môi trường từ file .env
//		Dotenv dotenv = Dotenv.configure()
//				.directory("/etc/secrets")
//				.filename("SYSTEM32.env")
//				.load();
//
//		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
//		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
//		System.setProperty("FACEBOOK_CLIENT_ID", dotenv.get("FACEBOOK_CLIENT_ID"));
//		System.setProperty("FACEBOOK_CLIENT_SECRET", dotenv.get("FACEBOOK_CLIENT_SECRET"));
//		System.setProperty("GITHUB_CLIENT_ID", dotenv.get("GITHUB_CLIENT_ID"));
//		System.setProperty("GITHUB_CLIENT_SECRET", dotenv.get("GITHUB_CLIENT_SECRET"));
//		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
//		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
//		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
//		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
//		System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));

		// Khởi động ứng dụng Spring Boot
		SpringApplication.run(Application.class, args);
	}
}
