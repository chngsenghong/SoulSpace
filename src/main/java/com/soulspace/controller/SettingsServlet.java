package com.soulspace.controller;

import java.io.IOException;

import com.soulspace.model.UserProfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Create Mock User Data (Replace with Database fetch later)
        UserProfile user = new UserProfile(
            "John", 
            "Doe", 
            "john.doe@example.com", 
            "I'm focusing on mindfulness and stress management techniques.",
            "Member",
            true,  // Email notifications ON
            false  // Push notifications OFF
        );

        // 2. Attach to request
        request.setAttribute("user", user);

        // 3. Forward to View
        request.getRequestDispatcher("/WEB-INF/views/settings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // This is where you would handle saving the form data
        // For now, just redirect back to the settings page
        response.sendRedirect(request.getContextPath() + "/settings?saved=true");
    }
}