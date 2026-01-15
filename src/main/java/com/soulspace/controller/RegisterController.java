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

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    // 1. Show the Registration Page
    @GetMapping
    public String showRegisterPage() {
        return "register";
    }

    // 2. Handle the Form Submission
    @PostMapping
    public String handleRegistration(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        // Check if user already exists
        if (userService.getUserByEmail(email) != null) {
            model.addAttribute("errorMessage", "That email is already registered. Please log in.");
            return "register";
        }

        // Create new User
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password); // Note: In production, hash this password!
        
        // Set Defaults
        newUser.setRole("STUDENT"); // Default role
        newUser.setBio("I am new to SoulSpace.");
        newUser.setEmailNotifications(true);
        newUser.setPushNotifications(false);

        // Save to DB
        userService.registerUser(newUser);

        // Redirect to login with success message
        return "redirect:/login?registered=true";
    }
}