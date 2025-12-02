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
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Check if user is ALREADY logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // If yes, send them to the Dashboard Servlet (NOT the JSP)
            response.sendRedirect("dashboard");
        } else {
            // If no, show the Login Page (Server can access WEB-INF)
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // --- VALIDATION ---
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            
            // A. Login Success
            HttpSession session = request.getSession();
            
            // Extract display name (e.g., "john" from "john@email.com")
            String displayName = email.split("@")[0];
            displayName = displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
            
            session.setAttribute("user", displayName);
            session.setAttribute("email", email);
            
            // CRITICAL FIX: Redirect to the Servlet URL pattern, NOT the JSP file
            response.sendRedirect("dashboard");
            
        } else {
            // B. Login Failed
            request.setAttribute("errorMessage", "Invalid email or password");
            // Forward back to the hidden login page so they can try again
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}