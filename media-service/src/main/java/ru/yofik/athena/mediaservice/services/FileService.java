package ru.yofik.athena.mediaservice.services;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileService {
    private final MinioClient minioClient;

    @Autowired
    public FileService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public HttpStatus uploadFile(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String UUid = String.valueOf(UUID.randomUUID());

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("athena-test-bucket")
                        .object(UUid)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), 0)
                        .build()
        );

        return HttpStatus.CREATED;
    }

    public ResponseEntity<?> getFile(String UUid) throws InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {
        if (UUid.length() != 36) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            InputStreamResource resource = new InputStreamResource(minioClient.getObject(GetObjectArgs.builder()
                    .bucket("athena-test-bucket")
                    .object(UUid)
                    .build())
            );
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (IOException | ErrorResponseException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public HttpStatus deleteFile(String UUid) throws ServerException, InsufficientDataException,  NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (UUid.length() != 36) {
            return HttpStatus.BAD_REQUEST;
        }

        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("athena-test-bucket")
                    .object(UUid)
                    .build()
            );
            return HttpStatus.OK;

        } catch (IOException | ErrorResponseException e) {
            return HttpStatus.NOT_FOUND;
        }
    }
}
