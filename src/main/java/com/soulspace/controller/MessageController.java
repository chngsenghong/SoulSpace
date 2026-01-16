package com.soulspace.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.model.Message;
import com.soulspace.model.User;
import com.soulspace.service.MessageService;
import com.soulspace.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/messaging")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    // --- MAIN MESSAGING PAGE ---
    @GetMapping
    public String messaging(
            @RequestParam(value = "chatWith", required = false) Long chatWith,
            HttpSession session,
            Model model) {

        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role"); // Get Role

        if (userId == null) return "redirect:/login";

        // 1. Load Conversations (Common for everyone)
        Map<User, Message> activeConversations = messageService.getActiveConversations(userId);
        Map<User, Message> historyConversations = messageService.getArchivedConversations(userId);

        // 2. Load Professionals List (Only needed for Students)
        if ("STUDENT".equals(role)) {
            model.addAttribute("professionals", userService.getProfessionals());
        }

        // 3. Handle Active Chat Window (Right Side)
        String selectedTab = "active";
        
        if (chatWith != null) {
            List<Message> messages = messageService.getChat(userId, chatWith);
            model.addAttribute("activeMessages", messages);
            model.addAttribute("activeChatId", chatWith);

            User partner = userService.getUserById(chatWith);
            if (partner != null) {
                model.addAttribute("activePartner", partner);

                // Check if this partner is new (not in active/history yet)
                boolean existsInActive = activeConversations.keySet().stream().anyMatch(u -> u.getId().equals(chatWith));
                boolean existsInHistory = historyConversations.keySet().stream().anyMatch(u -> u.getId().equals(chatWith));

                if (!existsInActive && !existsInHistory) {
                    // Add them to the map temporarily so they appear in the sidebar
                    Map<User, Message> newMap = new java.util.LinkedHashMap<>();
                    newMap.put(partner, null); 
                    newMap.putAll(activeConversations);
                    activeConversations = newMap;
                }
            }

            // Determine Status (Active vs Archived)
            String currentStatus = "ACTIVE";
            if (!messages.isEmpty()) {
                currentStatus = messages.get(0).getConversationStatus();
            }
            model.addAttribute("chatStatus", currentStatus);

            if ("ARCHIVED".equals(currentStatus)) {
                selectedTab = "history";
            }
        }

        model.addAttribute("conversations", activeConversations);
        model.addAttribute("historyConversations", historyConversations);
        model.addAttribute("selectedTab", selectedTab);

        // --- ROUTING LOGIC ---
        if ("STUDENT".equals(role)) {
            return "messaging"; // Students get the standard view
        } else {
            return "messaging-professional"; // Professionals & Faculty get the inbox view
        }
    }

    // --- SEND MESSAGE ---
    @PostMapping("/send")
    public String sendMessage(
            @RequestParam("receiverId") Long receiverId,
            @RequestParam("content") String content,
            HttpSession session) {

        Long senderId = (Long) session.getAttribute("userId");
        messageService.sendMessage(senderId, receiverId, content);
        return "redirect:/messaging?chatWith=" + receiverId;
    }

    // --- END CHAT ---
    @PostMapping("/end")
    public String endChat(@RequestParam("partnerId") Long partnerId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        messageService.archiveChat(userId, partnerId);
        return "redirect:/messaging";
    }

    // --- RESTART CHAT ---
    @PostMapping("/restart")
    public String restartChat(@RequestParam("partnerId") Long partnerId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        messageService.unarchiveChat(userId, partnerId);
        return "redirect:/messaging?chatWith=" + partnerId;
    }
}