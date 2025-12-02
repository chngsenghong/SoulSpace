package com.soulspace.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        
        // 1. Create Mock Data (Later replace this with Database calls)
        List<ForumPost> posts = new ArrayList<>();

        // Pinned Post
        ForumPost pinnedPost = new ForumPost(
            1,
            "Welcome to the SoulSpace Community Forum!",
            "We're thrilled to have you here. This is a safe space for sharing experiences, seeking support, and connecting with others.",
            "Admin", "AD", "#2563eb", "General",
            Arrays.asList("Announcement", "Community"),
            45, 1200, "2 days ago", true
        );
        posts.add(pinnedPost);

        // Regular Post 1
        posts.add(new ForumPost(
            2,
            "How I overcame my anxiety through mindfulness",
            "I wanted to share my journey of how practicing mindfulness for just 10 minutes a day completely changed my relationship with anxiety...",
            "Sarah M.", "SM", "#9333ea", "Anxiety",
            Arrays.asList("Anxiety", "Success Story", "Mindfulness"),
            23, 345, "3 hours ago", false
        ));

        // Regular Post 2
        posts.add(new ForumPost(
            3,
            "Seeking advice on managing work stress",
            "I've been feeling overwhelmed with work lately and it's affecting my sleep and mood. Has anyone found effective strategies?",
            "John D.", "JD", "#10b981", "Stress",
            Arrays.asList("Stress", "Work-Life Balance", "Advice Needed"),
            12, 189, "5 hours ago", false
        ));

        // Regular Post 3
        posts.add(new ForumPost(
            4,
            "Self-care routine that changed my life",
            "After struggling for years, I finally found a self-care routine that works for me. It includes morning meditation and journaling...",
            "Emily R.", "EM", "#f59e0b", "Self-Care",
            Arrays.asList("Self-Care", "Routine", "Success Story"),
            31, 567, "1 day ago", false
        ));

        // 2. Attach data to request
        request.setAttribute("forumPosts", posts);

        // 3. Forward to the View (JSP)
        request.getRequestDispatcher("/WEB-INF/views/forum.jsp").forward(request, response);
    }
}