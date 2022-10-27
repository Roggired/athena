package ru.yofik.athena.mediaservice.controllers;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/files")
public class FileController {
    private final MinioClient minioClient;
    @Autowired
    public FileController(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostMapping
    public ResponseEntity uploadFile(@RequestBody MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("athena-test-bucket").build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("athena-test-bucket").build());
        }

        minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket("athena-test-bucket")
                .object(file.getName())
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build()
        );

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{object}")
    public void getObject(@PathVariable("object") String object) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.getObject(GetObjectArgs.builder()
                .bucket("athena-test-bucket")
                .object(object)
                .build()
        );
    }

}
