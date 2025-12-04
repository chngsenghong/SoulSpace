package com.soulspace.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.soulspace.model.Assessment;
import com.soulspace.model.Recommendation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. User Info
        request.setAttribute("userName", "John Doe"); // Later from Session


        // 3. Latest Assessment
        request.setAttribute("assessment", new Assessment(
            "Stress Level Assessment", 
            "Moderate", 
            "yellow", 
            "Nov 3, 2025"
        ));

        // 4. Recommendations List
        List<Recommendation> recs = new ArrayList<>();
        recs.add(new Recommendation("Understanding Anxiety", "Learning Module", "15 min", 60, "brain"));
        recs.add(new Recommendation("Mindfulness Techniques", "Exercise", "10 min", 0, "meditation"));
        recs.add(new Recommendation("CBT Basics", "Learning Module", "20 min", 30, "brain"));
        
        request.setAttribute("recommendations", recs);

        // 5. Forward to View
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}