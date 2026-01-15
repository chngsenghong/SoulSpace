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
@RequestMapping("/forum/post")
public class ForumDetailController {

    private final ForumService forumService;
    private final UserService userService;

    @Autowired
    public ForumDetailController(ForumService forumService, UserService userService) {
        this.forumService = forumService;
        this.userService = userService;
    }

    @GetMapping
    public String showPostDetail(@RequestParam("id") Long id, Model model) {
        if (id != null) {
            // Use Service to get from DB
            ForumPost post = forumService.getPostById(id);
            
            if (post != null) {
                model.addAttribute("post", post);
                return "forum-detail";
            }
        }
        return "redirect:/forum";
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam("postId") Long postId,
                            @RequestParam("content") String content,
                            HttpSession session) {
        
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login"; 
        }

        User user = userService.getUserByEmail(email);
        
        // Call the service
        forumService.addComment(postId, content, user);

        return "redirect:/forum/post?id=" + postId; 
    }
}