package com.soulspace.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.soulspace.model.User;
import com.soulspace.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user-management")
public class UserManagementController {

    private final UserService userService;

    @Autowired
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserList(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<User> userList = userService.getAllUsers();
        model.addAttribute("users", userList);

        return "user-management";
    }
}