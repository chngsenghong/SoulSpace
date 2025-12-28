package com.soulspace.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soulspace.model.SuggestionCard;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    @GetMapping
    public String showChatbot(Model model) {
        model.addAttribute("botName", "SoulSpace AI Guide");
        model.addAttribute("welcomeTitle", "How can I support you today?");
        model.addAttribute("welcomeSubtitle", "I'm here to provide mental health guidance, coping strategies, and emotional support. Feel free to share what's on your mind.");

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
        
        // Add more suggestions as needed...
        model.addAttribute("suggestionList", suggestions);

        return "chatbot";
    }
}