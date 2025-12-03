package com.soulspace.controller;

import java.io.IOException;
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ForumPost> posts = ForumData.getAllPosts();
        
        request.setAttribute("forumPosts", posts);
        request.getRequestDispatcher("/WEB-INF/views/forum.jsp").forward(request, response);
    }
}