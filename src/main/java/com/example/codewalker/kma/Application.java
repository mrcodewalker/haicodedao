package com.example.codewalker.kma;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// Tải biến môi trường từ file .env
		Dotenv dotenv = Dotenv.configure()
				.directory("/etc/secrets")
				.filename("SYSTEM32")
				.load();

		System.setProperty("GOOGLE_CLIENT_ID", System.getenv("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", System.getenv("GOOGLE_CLIENT_SECRET"));
		System.setProperty("FACEBOOK_CLIENT_ID", System.getenv("FACEBOOK_CLIENT_ID"));
		System.setProperty("FACEBOOK_CLIENT_SECRET", System.getenv("FACEBOOK_CLIENT_SECRET"));
		System.setProperty("GITHUB_CLIENT_ID", System.getenv("GITHUB_CLIENT_ID"));
		System.setProperty("GITHUB_CLIENT_SECRET", System.getenv("GITHUB_CLIENT_SECRET"));
		System.setProperty("SPRING_DATASOURCE_URL", System.getenv("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", System.getenv("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", System.getenv("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("JWT_SECRET", System.getenv("JWT_SECRET"));
		System.setProperty("JWT_EXPIRATION", System.getenv("JWT_EXPIRATION"));


		// Khởi động ứng dụng Spring Boot
		SpringApplication.run(Application.class, args);
	}
}
