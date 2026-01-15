package com.soulspace.controller;

import java.util.List;

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
        
        // Use the new filter method
        List<ForumPost> posts = forumService.filterPosts(query, category, sort);
        
        model.addAttribute("forumPosts", posts);
        
        // Keep the current selection in the model to highlight UI buttons
        model.addAttribute("searchQuery", query);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentSort", sort);
        
        return "forum";
    }
    
    @PostMapping
    public String handleForumAction(
            @RequestParam("action") String action,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tags", required = false) String tagsInput,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 1. Get Logged-in User
        String email = (String) session.getAttribute("email");
        User user = (email != null) ? userService.getUserByEmail(email) : null;

        if (user == null) {
            return "redirect:/login";
        }

        if ("create".equals(action)) {
            // 1. Create the post object
            ForumPost newPost = new ForumPost(user, title, content, category, tagsInput);
            
            // 2. Save it (Service will set status to PENDING if triggers found)
            forumService.addPost(newPost);
            
            // 3. CHECK STATUS: Did it get flagged?
            if (newPost.getStatus() == com.soulspace.model.PostStatus.PENDING_REVIEW) {
                // Pass a flash attribute to trigger the modal
                redirectAttributes.addFlashAttribute("triggerAlert", true);
            }
        } else if (id != null) {
            ForumPost existingPost = forumService.getPostById(id);
            
            // 3. SECURITY UPDATE: Allow if Author OR Faculty
            boolean isAuthor = existingPost.getAuthor().getEmail().equals(email);
            boolean isFaculty = "FACULTY".equals(user.getRole()); // NEW CHECK

            if (existingPost != null && (isAuthor || isFaculty)) {
                
                if ("delete".equals(action)) {
                    forumService.deletePost(id);
                    
                    // Redirect back to dashboard if that's where they came from
                    if (isFaculty) return "redirect:/dashboard"; 
                    return "redirect:/forum";
                } 
                else if ("update".equals(action)) {
                    forumService.updatePost(id, title, category, content);
                    
                    // Redirect back to dashboard for Faculty convenience
                    if (isFaculty) return "redirect:/dashboard";
                    return "redirect:/forum/post?id=" + id;
                }
            } else {
                System.out.println("UNAUTHORIZED ATTEMPT");
            }
        }
        
        return "redirect:/forum";
    }

    @PostMapping("/support")
    public String toggleSupport(@RequestParam("postId") Long postId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login"; 
        }

        User user = userService.getUserByEmail(email);
        forumService.toggleSupport(postId, user);

        // Reload the page to show the new state
        return "redirect:/forum/post?id=" + postId; 
    }
}