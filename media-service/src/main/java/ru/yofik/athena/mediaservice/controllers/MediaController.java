package ru.yofik.athena.mediaservice.controllers;

import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yofik.athena.mediaservice.services.FileService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    private final FileService fileService;

    @Autowired
    public MediaController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        HttpStatus status = fileService.uploadFile(file);
        return new ResponseEntity<>(status);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getFile(@PathVariable("id") String UUID) throws ServerException, InsufficientDataException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return fileService.getFile(UUID);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") String UUID) throws ServerException, InsufficientDataException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        HttpStatus status = fileService.deleteFile(UUID);
        return new ResponseEntity<>(status);
    }

}
