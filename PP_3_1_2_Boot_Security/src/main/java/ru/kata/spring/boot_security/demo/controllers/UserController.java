package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userPage(Model model, Principal principal) {
        // Получаем Optional<User> из сервиса
        Optional<User> userOptional = userService.findByUsername(principal.getName());

        // Извлекаем пользователя, если его нет — выбрасываем исключение
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

        // Добавляем пользователя в модель
        model.addAttribute("user", user);
        return "user";
    }
}

