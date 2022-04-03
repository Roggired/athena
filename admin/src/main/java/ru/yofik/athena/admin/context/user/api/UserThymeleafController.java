package ru.yofik.athena.admin.context.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yofik.athena.admin.context.user.factory.UserFactory;
import ru.yofik.athena.admin.context.user.model.User;
import ru.yofik.athena.admin.context.user.service.UserService;

import javax.validation.Valid;

@Controller
public class UserThymeleafController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFactory userFactory;


    @GetMapping("/admin-panel/user")
    public String getUsersAdminPanel(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("login", true);
        return "admin-panel-users";
    }

    @PostMapping("/admin-panel/user")
    public String createUser(@Valid @ModelAttribute("user") User user, Model model) {
        try {
            userService.createUser(user.getName(), user.getLogin());
            model.addAttribute("login", true);
            model.addAttribute("invitation", "");
        } catch (Throwable t) {
            return "user-new :: client-form";
        }

        return "user-new :: success";
    }

    @GetMapping("/admin-panel/user/{id}")
    public String getUserView(@PathVariable("id") long id, Model model) {
        model.addAttribute(userService.getUser(id));
        model.addAttribute("login", true);
        return "user-view";
    }

    @GetMapping("/admin-panel/user/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin-panel/user";
    }

    @GetMapping("/admin-panel/user/{id}/lock")
    public String lockUser(@PathVariable("id") long id) {
        userService.lockUser(id);
        return "redirect:/admin-panel/user/" + id;
    }

    @GetMapping("/admin-panel/user/{id}/unlock")
    public String unlockUser(@PathVariable("id") long id) {
        userService.unlockUser(id);
        return "redirect:/admin-panel/user/" + id;
    }

    @PostMapping("/admin-panel/user/{id}/invitation")
    public String createNewInvitationForUser(@PathVariable("id") long id, Model model) {
        var invitation = userService.createNewInvitation(id);
        model.addAttribute("invitation", invitation.getCode());
        model.addAttribute("login", true);
        return "user-view :: success";
    }

    @GetMapping("/admin-panel/user/new")
    public String getNewUser(Model model) {
        model.addAttribute("user", userFactory.of("Новый пользователь", "new_user"));
        model.addAttribute("login", true);
        return "user-new";
    }
}
