package ru.yofik.athena.auth.api.thymeleaf;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yofik.athena.auth.api.rest.admin.requests.ChangePasswordForOtherAdminRequest;
import ru.yofik.athena.auth.api.rest.admin.requests.GenerateUserInvitationRequest;
import ru.yofik.athena.auth.api.rest.admin.requests.LockUserRequest;
import ru.yofik.athena.auth.api.rest.admin.requests.UnlockUserRequest;
import ru.yofik.athena.auth.api.rest.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.rest.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.rest.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.api.rest.user.views.UserShortView;
import ru.yofik.athena.auth.api.rest.user.views.UserView;
import ru.yofik.athena.auth.domain.admin.service.AdminService;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.common.domain.NewPage;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin-panel")
public class UsersMvcController {
    private final AdminService adminService;
    private final UserService userService;


    @GetMapping("/users-table")
    public String getUsersTable(
            Integer pageNumber,
            Model model,
            String login
    ) {
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        var pageMeta = new NewPage.Meta(
                List.of("id"),
                pageNumber,
                10,
                0L
        );

        var request = new FilteredUsersRequest();
        if (login != null) {
            request.login = login.trim();
        }

        var page = userService.getUsersPageable(pageMeta, request).map(UserShortView::from);
        var totalPages = page.getMeta().elementsTotal / page.getMeta().size;
        if (page.getMeta().elementsTotal % page.getMeta().size != 0) totalPages++;

        model.addAttribute("login", login);
        model.addAttribute("users", page.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page.getMeta().sequentialNumber);
        var leftPageNumber = page.getMeta().sequentialNumber - 1;
        if (leftPageNumber < 0) leftPageNumber = 0;
        model.addAttribute("leftPageNumber", leftPageNumber);
        var rightPageNumber = page.getMeta().sequentialNumber + 1;
        if (rightPageNumber >= totalPages) rightPageNumber = (int) totalPages - 1;
        model.addAttribute("rightPageNumber", rightPageNumber);

        return "users";
    }

    @GetMapping("/user-views/new-user")
    public String newUserView() {
        return "user-new";
    }

    @GetMapping("/user-views/new-admin")
    public String newAdminView() {
        return "admin-new";
    }

    @PostMapping("/actions/create-user")
    public String actionCreateUser(
            @RequestParam("login") String login,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("password") String password
    ) {
        login = login.trim();
        email = email.trim();
        role = role.trim();
        password = password.trim();

        var createUserRequest = new CreateUserRequest();
        createUserRequest.login = login;
        createUserRequest.email = email;
        createUserRequest.role = Role.valueOf(role);
        createUserRequest.withNotification = true;
        if (createUserRequest.role == Role.ADMIN) {
            createUserRequest.password = password;
        }

        var user = UserView.from(userService.createUser(createUserRequest));
        return "redirect:/admin-panel/user-views/view?id=" + user.id;
    }

    @GetMapping("/user-views/view")
    public String concreteUserView(long id, Model model) {
        var user = UserView.from(userService.getUserById(id));
        model.addAttribute("user", user);
        return "user-view";
    }

    @PostMapping("/actions/update-user")
    public String actionUpdateUser(
            @RequestParam("id") long id,
            @RequestParam("login") String login,
            @RequestParam("email") String email
    ) {
        login = login.trim();
        email = email.trim();

        var updateUserRequest = new UpdateUserRequest();
        updateUserRequest.login = login;
        updateUserRequest.email = email;

        userService.updateUser(id, updateUserRequest);
        return "redirect:/admin-panel/user-views/view?id=" + id;
    }

    @PostMapping("/actions/lock-user")
    public String actionLockUser(
            @RequestParam("id") long id
    ) {
        var request = new LockUserRequest();
        request.userId = id;
        request.reason = "Manual lock";
        adminService.lockUser(request);
        return "redirect:/admin-panel/user-views/view?id=" + id;
    }

    @PostMapping("/actions/unlock-user")
    public String actionUnlockUser(
            @RequestParam("id") long id
    ) {
        var request = new UnlockUserRequest();
        request.userId = id;
        adminService.unlockUser(request);
        return "redirect:/admin-panel/user-views/view?id=" + id;
    }

    @PostMapping("/actions/generate-invitation")
    public String actionGenerateInvitation(
            @RequestParam("id") long id,
            Model model
    ) {
        var request = new GenerateUserInvitationRequest();
        request.userId = id;
        request.withNotification = true;
        var invitation = adminService.generateUserInvitation(request);
        var user = UserView.from(userService.getUserById(id));
        model.addAttribute("user", user);
        model.addAttribute("invitation", invitation.newInvitation);
        return "user-view";
    }

    @PostMapping("/actions/delete-user")
    public String actionDeleteUser(
            @RequestParam("id") long id
    ) {
        userService.deleteUser(id);
        return "redirect:/admin-panel/users-table";
    }

    @PostMapping("/actions/set-admin-password")
    public String actionSetAdminPassword(
            @RequestParam("id") long id,
            @RequestParam("newPassword") String newPassword,
            Model model
    ) {
        newPassword = newPassword.trim();

        if (newPassword.isBlank()) {
            var user = UserView.from(userService.getUserById(id));
            model.addAttribute("user", user);
            return "user-view";
        }

        var request = new ChangePasswordForOtherAdminRequest();
        request.userId = id;
        request.newPassword = newPassword;

        adminService.changePasswordForOtherAdmin(request);
        var user = UserView.from(userService.getUserById(id));
        model.addAttribute("user", user);
        model.addAttribute("successfullySetPassword", true);
        return "user-view";
    }
}
