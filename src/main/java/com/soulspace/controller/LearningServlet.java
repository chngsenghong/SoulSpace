package com.soulspace.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/learning")
public class LearningServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // In a real application, these would come from a database (Model)
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("start".equals(action)) {
            String courseId = request.getParameter("courseId");
            startCourse(request, response, courseId);
            return;
        } else if ("download".equals(action)) {
            String certId = request.getParameter("certId");
            downloadCertificate(request, response, certId);
            return;
        }
        
        // Default action: Load the page
        loadLearningPage(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void loadLearningPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Prepare Mock Stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("coursesInProgress", 3);
        stats.put("certificatesEarned", 3);
        stats.put("learningTime", "24h");
        request.setAttribute("stats", stats);
        
        // 2. Prepare Featured Course
        Map<String, String> featuredCourse = new HashMap<>();
        featuredCourse.put("id", "feat-001");
        featuredCourse.put("title", "Mastering Mindfulness & Meditation");
        featuredCourse.put("description", "Learn evidence-based mindfulness techniques to reduce stress, improve focus, and enhance emotional well-being.");
        request.setAttribute("featuredCourse", featuredCourse);
        
        // 3. Prepare All Courses List
        List<Map<String, Object>> allCourses = getAllCourses();
        request.setAttribute("courses", allCourses);
        
        // 4. Filter for "In Progress" courses
        List<Map<String, Object>> inProgressCourses = new ArrayList<>();
        for (Map<String, Object> course : allCourses) {
            int progress = (Integer) course.get("progress");
            if (progress > 0 && progress < 100) {
                // Calculate lessons completed for display
                int totalLessons = (Integer) course.get("lessonsCount");
                int completedLessons = (int) Math.ceil((progress / 100.0) * totalLessons);
                course.put("completedLessons", completedLessons);
                inProgressCourses.add(course);
            }
        }
        request.setAttribute("inProgressCourses", inProgressCourses);
        
        // 5. Prepare Certificates
        List<Map<String, Object>> certificates = getCertificates();
        request.setAttribute("certificates", certificates);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/learning.jsp").forward(request, response);
    }
    
    // Helper method to generate mock course data
    private List<Map<String, Object>> getAllCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        
        // Course 1
        Map<String, Object> c1 = new HashMap<>();
        c1.put("id", "c1");
        c1.put("title", "Understanding Anxiety");
        c1.put("description", "Practical techniques to manage anxiety.");
        c1.put("category", "Anxiety");
        c1.put("duration", "2h 30m");
        c1.put("lessonsCount", 12);
        c1.put("progress", 75); // 75% done
        c1.put("thumbnailStyle", "");
        c1.put("iconSvg", "<svg fill='none' viewBox='0 0 24 24' stroke='currentColor' stroke-width='2'><path stroke-linecap='round' stroke-linejoin='round' d='M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z' /></svg>");
        courses.add(c1);
        
        // Course 2
        Map<String, Object> c2 = new HashMap<>();
        c2.put("id", "c2");
        c2.put("title", "Self-Compassion");
        c2.put("description", "Develop a kinder relationship with yourself.");
        c2.put("category", "Self-Care");
        c2.put("duration", "1h 45m");
        c2.put("lessonsCount", 8);
        c2.put("progress", 30); // 30% done
        c2.put("thumbnailStyle", "background: linear-gradient(135deg, #fef3c7 0%, #fed7aa 100%);");
        c2.put("iconSvg", "<svg fill='none' viewBox='0 0 24 24' stroke='currentColor' stroke-width='2' style='color: #f59e0b;'><path stroke-linecap='round' stroke-linejoin='round' d='M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z' /></svg>");
        courses.add(c2);
        
        // Course 3 (New)
        Map<String, Object> c3 = new HashMap<>();
        c3.put("id", "c3");
        c3.put("title", "Sleep Hygiene");
        c3.put("description", "Master the science of sleep.");
        c3.put("category", "Health");
        c3.put("duration", "3h 15m");
        c3.put("lessonsCount", 15);
        c3.put("progress", 0); // New course
        c3.put("thumbnailStyle", "background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);");
        c3.put("iconSvg", "<svg fill='none' viewBox='0 0 24 24' stroke='currentColor' stroke-width='2' style='color: #059669;'><path stroke-linecap='round' stroke-linejoin='round' d='M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z' /></svg>");
        courses.add(c3);

        return courses;
    }
    
    private List<Map<String, Object>> getCertificates() {
        List<Map<String, Object>> certificates = new ArrayList<>();
        
        Map<String, Object> cert1 = new HashMap<>();
        cert1.put("id", "cert1");
        cert1.put("courseName", "Mindfulness Fundamentals");
        cert1.put("completionDate", "Nov 15, 2025");
        certificates.add(cert1);
        
        Map<String, Object> cert2 = new HashMap<>();
        cert2.put("id", "cert2");
        cert2.put("courseName", "Stress Management");
        cert2.put("completionDate", "Oct 20, 2025");
        certificates.add(cert2);
        
        Map<String, Object> cert3 = new HashMap<>();
        cert3.put("id", "cert3");
        cert3.put("courseName", "CBT Basics");
        cert3.put("completionDate", "Sep 05, 2025");
        certificates.add(cert3);
        
        return certificates;
    }
    
    private void startCourse(HttpServletRequest request, HttpServletResponse response, String courseId) 
            throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("lastCourseStarted", courseId);
        
        // In a real app, you would redirect to course_detail.jsp
        // For now, we redirect back to learning page for demonstration
        System.out.println("Starting course: " + courseId);
        response.sendRedirect("learning"); 
    }
    
    private void downloadCertificate(HttpServletRequest request, HttpServletResponse response, String certId) 
            throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h3>Downloading Certificate ID: " + certId + "</h3>");
        response.getWriter().println("<p>Simulating PDF download...</p>");
        response.getWriter().println("<a href='learning'>Go Back</a>");
    }
}