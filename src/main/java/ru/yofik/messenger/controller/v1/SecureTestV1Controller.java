package ru.yofik.messenger.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secureTest")
public class SecureTestV1Controller {
    @GetMapping
    public String helloWorld() {
        return "Hello, World!";
    }
}
