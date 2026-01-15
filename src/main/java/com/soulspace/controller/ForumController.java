package com.soulspace.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors; // Import this

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String showForum(@RequestParam(value = "query", required = false) String query,
                            @RequestParam(value = "category", required = false) String category,
                            @RequestParam(value = "sort", required = false) String sort,
                            Model model) {
        
        List<ForumPost> posts = forumService.filterPosts(query, category, sort);
        model.addAttribute("forumPosts", posts);

        List<String> categories = Arrays.asList(
            "Academic Stress", "Mental Health", "Relationships", 
            "Campus Life", "Confessions", "Encouragement", "Others"
        );
        
        List<String> predefinedTags = Arrays.asList(
            "Anxiety", "Depression", "Exams", "Loneliness", 
            "Advice Needed", "Rant", "Sleep", "Success Story"
        );

        model.addAttribute("searchQuery", query);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentSort", sort);
        model.addAttribute("categories", categories);
        model.addAttribute("predefinedTags", predefinedTags);
        model.addAttribute("isHistory", false); // Default view
        
        return "forum";
    }

    // --- NEW: HISTORY ENDPOINT ---
    @GetMapping("/history")
    public String showHistory(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        User user = userService.getUserByEmail(email);

        // 1. Get All Posts and Filter by Current User ID
        // (This allows us to reuse the list without changing Service code)
        List<ForumPost> allPosts = forumService.getAllPosts(); // Assuming this method exists as seen in DashboardController
        List<ForumPost> myPosts = allPosts.stream()
                .filter(p -> p.getAuthor().getId().equals(user.getId()))
                .collect(Collectors.toList());

        model.addAttribute("forumPosts", myPosts);

        // 2. Add UI Data (So the page doesn't crash)
        List<String> categories = Arrays.asList(
            "Academic Stress", "Mental Health", "Relationships", 
            "Campus Life", "Confessions", "Encouragement", "Others"
        );
        model.addAttribute("categories", categories);
        model.addAttribute("isHistory", true); // Flag to change Title/Buttons

        return "forum";
    }
    
    // ... (Keep your existing @PostMapping logic unchanged) ...
    @PostMapping
    public String handleForumAction(
            @RequestParam("action") String action,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tags", required = false) List<String> tags,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // ... (Keep existing implementation) ...
        // Copy the implementation from your uploaded file if you are replacing the whole file
        // Or just add the GetMapping above to your existing file.
        
        // Shortened for brevity in this snippet - DO NOT DELETE your existing POST logic
        String email = (String) session.getAttribute("email");
        User user = (email != null) ? userService.getUserByEmail(email) : null;
        if (user == null) return "redirect:/login";

        if ("create".equals(action)) {
            ForumPost newPost = new ForumPost(user, title, content, category, tags);
            forumService.addPost(newPost);
            if (newPost.getStatus() == com.soulspace.model.PostStatus.PENDING_REVIEW) {
                redirectAttributes.addFlashAttribute("triggerAlert", true);
            }
        } else if (id != null) {
            ForumPost existingPost = forumService.getPostById(id);
            boolean isAuthor = existingPost.getAuthor().getEmail().equals(email);
            boolean isFaculty = "FACULTY".equals(user.getRole());

            if (existingPost != null && (isAuthor || isFaculty)) {
                if ("delete".equals(action)) {
                    forumService.deletePost(id);
                    if (isFaculty) return "redirect:/dashboard"; 
                    return "redirect:/forum";
                } 
                else if ("update".equals(action)) {
                    forumService.updatePost(id, title, category, content);
                    if (isFaculty) return "redirect:/dashboard";
                    return "redirect:/forum/post?id=" + id;
                }
            }
        }
        return "redirect:/forum";
    }

    @PostMapping("/support")
    public String toggleSupport(@RequestParam("postId") Long postId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login"; 
        User user = userService.getUserByEmail(email);
        forumService.toggleSupport(postId, user);
        return "redirect:/forum/post?id=" + postId; 
    }
}