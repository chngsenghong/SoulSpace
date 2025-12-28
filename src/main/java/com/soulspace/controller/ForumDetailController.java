package com.soulspace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.model.ForumPost;
import com.soulspace.service.ForumService;

@Controller
@RequestMapping("/forum/post")
public class ForumDetailController {

    private final ForumService forumService;

    @Autowired
    public ForumDetailController(ForumService forumService) {
        this.forumService = forumService;
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
}