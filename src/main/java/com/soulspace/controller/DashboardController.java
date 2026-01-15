package com.soulspace.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.model.Appointment;
import com.soulspace.model.Assessment;
import com.soulspace.model.ForumPost;
import com.soulspace.model.Recommendation; // Import this
import com.soulspace.service.AppointmentService;
import com.soulspace.service.ForumService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final AppointmentService appointmentService;
    private final ForumService forumService; 

    @Autowired
    public DashboardController(AppointmentService appointmentService, ForumService forumService) {
        this.appointmentService = appointmentService;
        this.forumService = forumService;
    }

    @GetMapping
    public String showDashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        Long userId = (Long) session.getAttribute("userId");

        // --- ROLE 1: MENTAL HEALTH PROFESSIONAL ---
        if ("PROFESSIONAL".equals(role)) {
             // They only care about appointments
             model.addAttribute("allAppointments", appointmentService.getAppointmentsByUser(userId));
        } 
        
        else if ("FACULTY".equals(role)) {
            // 1. Fetch Pending (High Priority)
            List<ForumPost> pendingPosts = forumService.getPendingPosts();
            model.addAttribute("pendingPosts", pendingPosts);
            
            // 2. NEW: Fetch ALL Posts (For Management Table)
            // You might want to sort these differently (e.g., newest first)
            List<ForumPost> allPosts = forumService.getAllPosts(); 
            model.addAttribute("managePosts", allPosts);
        }

        // --- ROLE 3: STUDENT (Default) ---
        else {
            if (userId != null) {
                List<Appointment> apps = appointmentService.getAppointmentsByUser(userId);
                if (!apps.isEmpty()) {
                    model.addAttribute("nextAppointment", apps.get(0));
                }
            }
            // Mock Data for Student Dashboard
            model.addAttribute("assessment", new Assessment("Stress Level Assessment", "Moderate", "yellow", "Nov 3, 2025"));
            
            List<Recommendation> recs = new ArrayList<>();
            recs.add(new Recommendation("Understanding Anxiety", "Learning Module", "15 min", 45, "brain"));
            recs.add(new Recommendation("Morning Meditation", "Exercise", "10 min", 0, "meditation"));
            model.addAttribute("recommendations", recs);
        }

        return "dashboard";
    }

    // Updated: Only FACULTY can approve posts
    @PostMapping("/approve-post")
    public String approvePost(@RequestParam("postId") Long postId, HttpSession session) {
        String role = (String) session.getAttribute("role");
        
        if ("FACULTY".equals(role)) {
            forumService.approvePost(postId);
        }
        return "redirect:/dashboard";
    }
}