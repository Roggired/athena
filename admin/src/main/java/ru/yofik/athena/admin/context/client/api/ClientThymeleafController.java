package ru.yofik.athena.admin.context.client.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.admin.context.auth.service.AuthService;
import ru.yofik.athena.admin.context.client.api.request.LoginRequest;
import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.ClientPermission;
import ru.yofik.athena.admin.context.client.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;

@Controller
@Log4j2
public class ClientThymeleafController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthService authService;


    @GetMapping("/")
    public String getRootPage() {
        return "index";
    }

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

    @PostMapping(value = "/athena/login")
    public String login(@RequestBody LoginRequest request, HttpServletResponse servletResponse) {
        authService.login(request.getPassword(), servletResponse);
        return "redirect:/admin-panel";
    }

    // TODO переписать на REST
    @GetMapping(value = "/athena/logout")
    public String logout(HttpServletRequest servletRequest) {
        authService.logout();
        return "redirect:/index";
    }

    @GetMapping(value = "/error")
    public String error() {
        return "error";
    }

    @GetMapping("/admin-panel")
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("login", true);
        return "admin-panel";
    }

    @GetMapping("/admin-panel/client/{id}")
    public String getClient(@PathVariable("id") long id, Model model) {
        model.addAttribute("client", clientService.getClient(id));
        model.addAttribute("login", true);
        return "client-view";
    }

    @GetMapping("/admin-panel/client/new")
    public String newClient(Model model) {
        model.addAttribute("client", new Client(0L, "New client", true, new HashSet<>() {{add(ClientPermission.AUTHORIZE_USER);}}));
        model.addAttribute("clientPermissions", ClientPermission.values());
        model.addAttribute("login", true);
        return "client-new";
    }

    @PostMapping(value = "/admin-panel/client")
    public String registerClient(@Valid @ModelAttribute("client") Client client, BindingResult bindingResult, Model model) {
        try {
            var token = clientService.createClient(client.getName(), client.getClientPermissions());
            model.addAttribute("token", new String(token.getToken()));
            model.addAttribute("login", true);
        } catch (Throwable t) {
            return "client-new :: client-form";
        }

        return "client-new :: success";
    }

    @GetMapping(value = "/admin-panel/client/{id}/delete")
    public String deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
        return "redirect:/admin-panel";
    }

    @GetMapping(value = "/admin-panel/client/{id}/activate")
    public String activateClient(@PathVariable("id") long id, Model model) {
        clientService.activateClient(id);
        model.addAttribute("message", "Клиент активирован");
        model.addAttribute("client", clientService.getClient(id));
        model.addAttribute("login", true);
        return "redirect:/admin-panel/client/" + id;
    }

    @GetMapping(value = "/admin-panel/client/{id}/deactivate")
    public String deactivateClient(@PathVariable("id") long id, Model model) {
        clientService.deactivateClient(id);
        model.addAttribute("message", "Клиент деактивирован");
        model.addAttribute("client", clientService.getClient(id));
        model.addAttribute("login", true);
        return "redirect:/admin-panel/client/" + id;
    }

    @PostMapping(value = "/admin-panel/client/{id}/token")
    public String generateNewToken(@PathVariable("id") long id, Model model) {
        var token = clientService.generateNewToken(id);
        model.addAttribute("token", new String(token.getToken()));
        model.addAttribute("login", true);
        return "client-view :: success";
    }
}
