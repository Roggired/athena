package ru.yofik.athena.mediaservice;

import io.minio.MinioClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.Objects;

@SpringBootApplication
@PropertySource("classpath:application.yml")
public class MediaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaServiceApplication.class, args);
	}

	@Resource
	Environment environment;
	@Bean
	public MinioClient getMinioClient() {
		MinioClient minioClient = MinioClient
				.builder()
				.endpoint(Objects.requireNonNull(environment.getProperty("url")))
				.credentials(Objects.requireNonNull(environment.getProperty("access-key")), Objects.requireNonNull(environment.getProperty("secret-key")))
				.build();

		return minioClient;
	}

}
