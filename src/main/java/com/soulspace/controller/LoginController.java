package com.soulspace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.model.User;
import com.soulspace.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showLoginPage() {
        return "login";
    }

    @PostMapping
    public String handleLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("errorMessage", "Please enter email and password");
            return "login";
        }

        // 1. Fetch User from DB
        User dbUser = userService.getUserByEmail(email);

        // 2. Validate User & Password
        if (dbUser != null && dbUser.getPassword().equals(password)) {
            // 3. Set Session Attributes (CRITICAL: Save ID)
            session.setAttribute("user", dbUser.getFirstName() + " " + dbUser.getLastName());
            session.setAttribute("email", dbUser.getEmail());
            session.setAttribute("role", dbUser.getRole());
            session.setAttribute("userId", dbUser.getId()); // <--- Used by Dashboard

            return "redirect:/dashboard";
        } else {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        }
    }
}