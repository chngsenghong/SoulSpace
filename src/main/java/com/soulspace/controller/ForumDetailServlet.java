package com.soulspace.controller;

import java.io.IOException;

import com.soulspace.data.ForumData;
import com.soulspace.model.ForumPost;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/forum/post")
public class ForumDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Get the ID from the URL (e.g., ?id=1)
        String idParam = request.getParameter("id");
        
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                // 2. Find the post
                ForumPost post = ForumData.getPostById(id);
                
                if (post != null) {
                    request.setAttribute("post", post);
                    request.getRequestDispatcher("/WEB-INF/views/forum-detail.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid ID format
            }
        }
        
        // 3. If ID not found or invalid, go back to main forum
        response.sendRedirect(request.getContextPath() + "/forum");
    }
}