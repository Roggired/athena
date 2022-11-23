package ru.yofik.athena.mediaservice.minioconfig;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {
    private final MinioProperties minioProperties;
    private final MinioAccessProperties minioAccessProperties;

    @Autowired
    public MinioConfig(MinioProperties minioProperties, MinioAccessProperties minioAccessProperties) {
        this.minioProperties = minioProperties;
        this.minioAccessProperties = minioAccessProperties;
    }

    @Bean
    public MinioClient getMinioClient() {
        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioAccessProperties.getName(), minioAccessProperties.getSecret())
                .build();

        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("athena-test-bucket").build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("athena-test-bucket").build());
            }
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            throw new RuntimeException(e);
        }

        return minioClient;
    }
}
