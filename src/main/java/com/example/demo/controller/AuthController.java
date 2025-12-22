package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            // Use the selected role from the form
            if (user.getRole() == null) {
                user.setRole(User.Role.CUSTOMER); // fallback default
            }
            authService.registerUser(user);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (role.equals("ROLE_BANKER")) {
                return "redirect:/banker/dashboard";
            } else {
                return "redirect:/customer/dashboard";
            }
        }
        return "redirect:/login";
    }
}