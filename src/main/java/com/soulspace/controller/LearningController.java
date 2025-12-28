package com.soulspace.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/learning")
public class LearningController {

    @GetMapping
    public String handleLearningRequest(
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "certId", required = false) String certId,
            HttpSession session,
            Model model,
            HttpServletResponse response) throws IOException {

        if ("start".equals(action) && courseId != null) {
            session.setAttribute("lastCourseStarted", courseId);
            System.out.println("Starting course: " + courseId);
            return "redirect:/learning";
        } 
        
        if ("download".equals(action) && certId != null) {
            // In a real Spring app, you'd likely return a ResponseEntity<Resource>
            // For direct migration of servlet logic writing to stream:
            response.setContentType("text/html");
            response.getWriter().println("<h3>Downloading Certificate ID: " + certId + "</h3>");
            response.getWriter().println("<p>Simulating PDF download...</p>");
            response.getWriter().println("<a href='learning'>Go Back</a>");
            return null; // Response handled manually
        }

        // Default: Load Page Data
        loadLearningPageData(model);
        return "learning";
    }

    private void loadLearningPageData(Model model) {
        // Stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("coursesInProgress", 3);
        stats.put("certificatesEarned", 5);
        stats.put("learningTime", "12h 30m");
        model.addAttribute("stats", stats);

        // Featured Course
        Map<String, String> featured = new HashMap<>();
        featured.put("id", "c101");
        featured.put("title", "Mastering Emotional Resilience");
        featured.put("description", "Learn how to bounce back from setbacks and build mental strength.");
        model.addAttribute("featuredCourse", featured);

        // Courses List (Mock)
        List<Map<String, Object>> courses = getMockCourses();
        model.addAttribute("courses", courses);
        
        // In Progress (Mock - subset of courses)
        List<Map<String, Object>> inProgress = new ArrayList<>();
        if(!courses.isEmpty()) inProgress.add(courses.get(0));
        model.addAttribute("inProgressCourses", inProgress);

        // Certificates (Mock)
        model.addAttribute("certificates", getMockCertificates());
    }

    // Helper methods getMockCourses() and getMockCertificates() 
    // would contain the same data population logic as in your Servlet.
    private List<Map<String, Object>> getMockCourses() {
         List<Map<String, Object>> courses = new ArrayList<>();
         // ... Add courses logic from servlet ...
         // Example:
         Map<String, Object> c1 = new HashMap<>();
         c1.put("id", "c1");
         c1.put("title", "Understanding Anxiety");
         c1.put("category", "Anxiety");
         c1.put("description", "Learn the biological and psychological mechanisms of anxiety.");
         c1.put("duration", "45 min");
         c1.put("lessonsCount", 5);
         c1.put("progress", 60);
         // Simplified SVG for brevity
         c1.put("iconSvg", "<svg>...</svg>"); 
         c1.put("thumbnailStyle", "background: linear-gradient(135deg, #e0f2fe 0%, #bae6fd 100%); color: #0284c7;");
         courses.add(c1);
         return courses;
    }

    private List<Map<String, Object>> getMockCertificates() {
        List<Map<String, Object>> certs = new ArrayList<>();
        Map<String, Object> cert1 = new HashMap<>();
        cert1.put("id", "cert1");
        cert1.put("courseName", "Mindfulness Basics");
        cert1.put("completionDate", "Nov 10, 2025");
        certs.add(cert1);
        return certs;
    }
}