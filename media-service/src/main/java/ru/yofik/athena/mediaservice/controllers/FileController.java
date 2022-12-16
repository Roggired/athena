package ru.yofik.athena.mediaservice.controllers;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("athena-test-bucket")
                        .object(file.getName())
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), 0)
                        .build()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{object}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getObject(@PathVariable("object") String object) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStreamResource resource = new InputStreamResource(minioClient.getObject(GetObjectArgs.builder()
                .bucket("athena-test-bucket")
                .object(object)
                .build())
        );

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
