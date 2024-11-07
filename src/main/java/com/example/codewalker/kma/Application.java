package com.example.codewalker.kma;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// Tải biến môi trường từ file .env
		Dotenv dotenv = Dotenv.configure()
				.directory(".") // Chỉ định thư mục chứa file .env
				.filename("SYSTEM32.env") // Tên file .env
				.load();

		// Thiết lập các thuộc tính hệ thống từ các biến môi trường
//		System.setProperty("server.port", dotenv.get("SERVER_PORT", "8080"));
		System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL", "jdbc:mysql://localhost:4306/clone"));
		System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME", "root"));
		System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD", "123456789"));
//		System.setProperty("spring.datasource.driver-class-name", dotenv.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "com.mysql.cj.jdbc.Driver"));
//		System.setProperty("spring.jpa.show-sql", dotenv.get("JPA_SHOW_SQL", "true"));
//		System.setProperty("spring.jpa.hibernate.ddl-auto", dotenv.get("JPA_HIBERNATE_DDL_AUTO", "none"));
//		System.setProperty("spring.jpa.properties.hibernate.dialect", dotenv.get("JPA_HIBERNATE_DIALECT", "org.hibernate.dialect.MySQL8Dialect"));
//		System.setProperty("spring.jpa.properties.hibernate.format_sql", dotenv.get("JPA_HIBERNATE_FORMAT_SQL", "true"));
		System.setProperty("api.prefix", dotenv.get("API_PREFIX", "api/v1"));
		System.setProperty("jwt.secret", dotenv.get("JWT_SECRET", "Z54uiPhveohL/uORp8a8rHhu0qalR4Mj+aIOz5ZA5zY="));
		System.setProperty("jwt.expiration", dotenv.get("JWT_EXPIRATION", "8640000"));
		System.setProperty("google.client.id", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("google.client.secret", dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.setProperty("google.redirect.uri", dotenv.get("GOOGLE_REDIRECT_URI"));
		System.setProperty("google.scope", dotenv.get("GOOGLE_SCOPE"));
		System.setProperty("facebook.client.id", dotenv.get("FACEBOOK_CLIENT_ID"));
		System.setProperty("facebook.client.secret", dotenv.get("FACEBOOK_CLIENT_SECRET"));
		System.setProperty("facebook.redirect.uri", dotenv.get("FACEBOOK_REDIRECT_URI"));
		System.setProperty("facebook.scope", dotenv.get("FACEBOOK_SCOPE"));
		System.setProperty("facebook.user.info.uri", dotenv.get("FACEBOOK_USER_INFO_URI"));
		System.setProperty("github.client.id", dotenv.get("GITHUB_CLIENT_ID"));
		System.setProperty("github.client.secret", dotenv.get("GITHUB_CLIENT_SECRET"));
		System.setProperty("github.redirect.uri", dotenv.get("GITHUB_REDIRECT_URI"));
		System.setProperty("github.scope", dotenv.get("GITHUB_SCOPE"));
		System.setProperty("cors.allowed.origins", dotenv.get("CORS_ALLOWED_ORIGINS"));

		// Khởi động ứng dụng Spring Boot
		SpringApplication.run(Application.class, args);
	}
}
