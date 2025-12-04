package com.soulspace.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.soulspace.data.ForumData;
import com.soulspace.model.ForumPost;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/forum")
public class ForumServlet extends HttpServlet {

    // READ
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("forumPosts", ForumData.getAllPosts());
        request.getRequestDispatcher("/WEB-INF/views/forum.jsp").forward(request, response);
    }

    // CREATE, UPDATE, DELETE
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            String title = request.getParameter("title");
            String category = request.getParameter("category");
            String content = request.getParameter("content");
            String tagsInput = request.getParameter("tags");
            
            // Convert comma-separated tags to List
            List<String> tags = new ArrayList<>();
            if(tagsInput != null && !tagsInput.isEmpty()) {
                tags = Arrays.asList(tagsInput.split(","));
            }

            // Create new Mock Post
            ForumPost newPost = new ForumPost(
                ForumData.getNextId(),
                title,
                content, // Using content as excerpt for now
                "You", "YO", "#10b981", // Mock Author
                category,
                tags,
                0, 0, "Just now", false
            );
            
            ForumData.addPost(newPost);

        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            ForumData.deletePost(id);

        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String category = request.getParameter("category");
            String content = request.getParameter("content");
            
            ForumData.updatePost(id, title, category, content);
        }

        // Redirect back to forum list to prevent re-submission on refresh
        response.sendRedirect(request.getContextPath() + "/forum");
    }
}