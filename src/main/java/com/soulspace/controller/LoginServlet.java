package com.soulspace.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (email != null && !email.isEmpty()) {
            HttpSession session = request.getSession();
            
            // 1. Determine Role based on Email
            String role = "STUDENT"; // Default
            String displayName = "Student";

            if (email.equalsIgnoreCase("professional@gmail.com")) {
                role = "PROFESSIONAL";
                displayName = "Dr. Sarah Johnson"; // Mock Name
            } else if (email.equalsIgnoreCase("student@gmail.com")) {
                role = "STUDENT";
                displayName = "Student User";
            } else {
                // Generic fallback
                displayName = email.split("@")[0];
            }

            // 2. Save to Session
            session.setAttribute("user", displayName);
            session.setAttribute("email", email);
            session.setAttribute("role", role); // <--- IMPORTANT
            
            response.sendRedirect("dashboard");
            
        } else {
            request.setAttribute("errorMessage", "Invalid credentials");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}