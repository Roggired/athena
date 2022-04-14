package ru.yofik.athena.admin.context.user.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.admin.context.user.api.request.CreateUserRequest;
import ru.yofik.athena.admin.context.user.api.response.GetAllUsersResponse;
import ru.yofik.athena.admin.context.user.api.response.GetUserResponse;
import ru.yofik.athena.admin.context.user.api.response.NewInvitationResponse;
import ru.yofik.athena.admin.context.user.service.UserService;

import javax.validation.Valid;

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
