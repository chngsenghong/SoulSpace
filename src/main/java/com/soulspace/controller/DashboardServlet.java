package com.soulspace.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// This Servlet listens for requests to "/dashboard"
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Security Check
        // Check if a session exists and if the "user" attribute is present
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            // If not logged in, force redirect back to the Login Servlet
            response.sendRedirect("login");
            return;
        }

        // 2. If Logged In, Forward to the Protected View
        // request.getRequestDispatcher() happens on the SERVER side, 
        // so it IS allowed to access WEB-INF.
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Treat POST requests as GET requests (load the page)
        doGet(request, response);
    }
}