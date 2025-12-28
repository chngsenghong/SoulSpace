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
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;

    @Autowired
    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSettings(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "settings"; 
    }

    @PostMapping
    public String saveSettings(
            // FIX: Added 'name = "..."' to every @RequestParam
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "bio", required = false) String bio,
            @RequestParam(name = "emailNotif", required = false) String emailNotif,
            @RequestParam(name = "pushNotif", required = false) String pushNotif,
            HttpSession session) {

        String currentEmail = (String) session.getAttribute("email");
        if (currentEmail == null) return "redirect:/login";

        User user = userService.getUserByEmail(currentEmail);

        if (user != null) {
            if (firstName != null) user.setFirstName(firstName);
            if (lastName != null) user.setLastName(lastName);
            
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
                session.setAttribute("email", email); 
            }
            
            if (bio != null) user.setBio(bio);

            // Checkboxes send "on" if checked, null if unchecked
            user.setEmailNotifications("on".equals(emailNotif));
            user.setPushNotifications("on".equals(pushNotif));

            userService.updateUserProfile(user);
            
            // Update the display name in the session immediately
            session.setAttribute("user", user.getFirstName() + " " + user.getLastName());
        }

        return "redirect:/settings?saved=true";
    }
}