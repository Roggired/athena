package ru.yofik.athena.mediaservice;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
				.endpoint(Objects.requireNonNull(environment.getProperty("minio.url")))
				.credentials(Objects.requireNonNull(environment.getProperty("minio.access.name")), Objects.requireNonNull(environment.getProperty("minio.access.secret")))
				.build();

		try {
			if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("athena-test-bucket").build())) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket("athena-test-bucket").build());
			}
		} catch (ErrorResponseException e) {
			throw new RuntimeException(e);
		} catch (InsufficientDataException e) {
			throw new RuntimeException(e);
		} catch (InternalException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (InvalidResponseException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (ServerException e) {
			throw new RuntimeException(e);
		} catch (XmlParserException e) {
			throw new RuntimeException(e);
		}

		return minioClient;
	}
}
