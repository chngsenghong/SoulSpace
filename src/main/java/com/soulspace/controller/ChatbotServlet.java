package com.soulspace.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.soulspace.model.SuggestionCard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/chatbot")
public class ChatbotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Setup Page Info
        request.setAttribute("botName", "SoulSpace AI Guide");
        request.setAttribute("welcomeTitle", "How can I support you today?");
        request.setAttribute("welcomeSubtitle", "I'm here to provide mental health guidance, coping strategies, and emotional support. Feel free to share what's on your mind.");

        // 2. Setup Suggestion Cards
        List<SuggestionCard> suggestions = new ArrayList<>();

        suggestions.add(new SuggestionCard(
            "Feeling Anxious", 
            "Get anxiety coping strategies", 
            "I'm feeling anxious",
            "M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
        ));

        suggestions.add(new SuggestionCard(
            "Managing Stress", 
            "Learn stress relief techniques", 
            "I'm feeling stressed",
            "M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"
        ));

        suggestions.add(new SuggestionCard(
            "Better Sleep", 
            "Improve sleep quality", 
            "I want to improve my sleep",
            "M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"
        ));

        suggestions.add(new SuggestionCard(
            "Mindfulness", 
            "Practice being present", 
            "Tell me about mindfulness",
            "M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
        ));

        request.setAttribute("suggestionList", suggestions);

        // --- THE FIX IS HERE ---
        // You MUST have the leading slash "/" before WEB-INF
        request.getRequestDispatcher("/WEB-INF/views/chatbot.jsp").forward(request, response);
    }
}