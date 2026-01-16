package com.soulspace.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soulspace.model.SuggestionCard;
import com.soulspace.service.GeminiService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired
    private GeminiService geminiService;

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

    @PostMapping(value = "/api/ask", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, String> askBot(@RequestBody Map<String, String> payload, HttpSession session) {
        String userMessage = payload.get("message");
        
        // --- 1. EMERGENCY SAFETY CHECK (Malaysian Context) ---
        // We check this BEFORE calling the AI to ensure they get local help immediately.
        if (isEmergency(userMessage)) {
            // WE RETURN HTML DIRECTLY so it renders bold correctly
            String malaysianSafetyMsg = 
                "⚠️ <strong>It sounds like you're going through a difficult time.</strong><br><br>" +
                "You are not alone. Please contact these Malaysian crisis resources immediately:<br><br>" +
                "📞 <strong>Befrienders KL:</strong> 03-7627 2929 (24/7, Free)<br>" +
                "📞 <strong>Talian Kasih:</strong> 15999 (24/7)<br>" +
                "🚑 <strong>Emergency:</strong> 999<br><br>" +
                "Please reach out to a professional or go to the nearest hospital.";
            
            return Map.of("response", malaysianSafetyMsg);
        }

        // --- 2. CHAT HISTORY (Context) ---
        List<Map<String, String>> history = (List<Map<String, String>>) session.getAttribute("chatHistory");
        if (history == null) {
            history = new ArrayList<>();
        }

        // Add User Message
        Map<String, String> userEntry = new HashMap<>();
        userEntry.put("role", "user");
        userEntry.put("content", userMessage);
        history.add(userEntry);

        // --- 3. GET AI RESPONSE ---
        String aiResponse = geminiService.getAIResponse(history);

        // Add Bot Response
        Map<String, String> botEntry = new HashMap<>();
        botEntry.put("role", "bot");
        botEntry.put("content", aiResponse);
        history.add(botEntry);

        // Save session & limit size
        session.setAttribute("chatHistory", history);
        if (history.size() > 10) history.subList(0, history.size() - 10).clear();

        return Map.of("response", aiResponse);
    }

    // Helper to detect crisis keywords
    private boolean isEmergency(String text) {
        if (text == null) return false;
        String lower = text.toLowerCase();
        return lower.contains("suicide") || 
               lower.contains("kill myself") || 
               lower.contains("want to die") || 
               lower.contains("end my life") ||
               lower.contains("hurt myself") ||
               lower.contains("no reason to live");
    }
}
