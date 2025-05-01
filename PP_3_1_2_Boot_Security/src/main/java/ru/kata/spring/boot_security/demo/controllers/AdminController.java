package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        List<User> users = userService.findAllUsers(); // или другой метод для получения списка пользователей
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-form";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user,
                          @RequestParam(required = false) List<Long> roleIds) {
        userService.saveUser(user, roleIds != null ? roleIds : Collections.emptyList());
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-form";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id,
                             @ModelAttribute User user,
                             @RequestParam(required = false) List<Long> roleIds) {
        userService.updateUser(id, user, roleIds);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}

