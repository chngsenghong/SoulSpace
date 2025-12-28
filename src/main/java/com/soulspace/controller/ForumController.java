package com.soulspace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.model.ForumPost;
import com.soulspace.model.User;
import com.soulspace.service.ForumService;
import com.soulspace.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;
    private final UserService userService;

    @Autowired
    public ForumController(ForumService forumService, UserService userService) {
        this.forumService = forumService;
        this.userService = userService;
    }

    @GetMapping
    public String showForum(Model model) {
        model.addAttribute("forumPosts", forumService.getAllPosts());
        return "forum";
    }
    
    @PostMapping
    public String handleForumAction(
            @RequestParam("action") String action,
            @RequestParam(value = "id", required = false) Long id, // Changed Integer to Long
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tags", required = false) String tagsInput,
            HttpSession session) {

        // Get logged in user
        String email = (String) session.getAttribute("email");
        User user = (email != null) ? userService.getUserByEmail(email) : null;

        if ("create".equals(action) && user != null) {
            
            // Your Entity expects tags as a single String (e.g. "Anxiety,Help")
            // The input comes as "Anxiety,Help" already, so we pass it directly.
            
            ForumPost newPost = new ForumPost(
                    user,       // The Author (User object)
                    title,      // Title
                    content,    // Content
                    category,   // Category
                    tagsInput   // Tags (String)
            );
            
            forumService.addPost(newPost);

        } else if ("delete".equals(action) && id != null) {
            forumService.deletePost(id);

        } 
        // Note: Update logic would need a specific method in Service that calls setters
        
        return "redirect:/forum";
    }
}