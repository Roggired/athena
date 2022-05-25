package ru.yofik.athena.admin.context.user.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yofik.athena.admin.context.user.api.request.CreateUserRequest;
import ru.yofik.athena.admin.context.user.api.response.GetAllUsersResponse;
import ru.yofik.athena.admin.context.user.api.response.GetUserResponse;
import ru.yofik.athena.admin.context.user.api.response.NewInvitationResponse;
import ru.yofik.athena.admin.context.user.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UserResource {
    @Autowired
    private UserService userService;


    @GetMapping
    public GetAllUsersResponse getAllUsers() {
        return new GetAllUsersResponse(
                userService.getAllUsers()
        );
    }

    @GetMapping("/{id}")
    public GetUserResponse getUser(
            @PathVariable("id") long id
    ) {
        return new GetUserResponse(
                userService.getUser(id)
        );
    }

    @PostMapping
    public void createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {
        userService.createUser(request.name, request.login);
    }

    @PostMapping(value = "/imported")
    public ResponseEntity<String> importUsers(
            @RequestParam("file") MultipartFile multipartFile
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userService.importUsers(multipartFile.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable("id") long id
    ) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/invitation")
    public NewInvitationResponse generateNewInvitation(
            @PathVariable("id") long id
    ) {
        return new NewInvitationResponse(
                userService.createNewInvitation(id).getCode()
        );
    }

    @PutMapping("/{id}/lock")
    public void lockUser(
            @PathVariable("id") long id
    ) {
        userService.lockUser(id);
    }

    @PutMapping("/{id}/unlock")
    public void unlockUser(
            @PathVariable("id") long id
    ) {
        userService.unlockUser(id);
    }
}
