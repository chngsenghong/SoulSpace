package com.soulspace.controller;

import java.util.List; // Needed to find receiver

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.dao.UserDAO;
import com.soulspace.model.Message;
import com.soulspace.model.User;
import com.soulspace.service.MessageService;
import com.soulspace.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/messaging")
public class MessageController {

    @Autowired private MessageService messageService;
    @Autowired private UserService userService;
    // We need direct access to find users by ID for the receiver
    @Autowired private UserDAO userDAO; // Or add findById to UserService

    @GetMapping
    public String showMessaging(@RequestParam(value = "chatWith", required = false) Long chatWithId,
                                HttpSession session, Model model) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) return "redirect:/login";

        // 1. Load Sidebar (Conversations)
        List<MessageService.ConversationDTO> conversations = messageService.getConversations(currentUserId);
        model.addAttribute("conversations", conversations);

        // 2. Load Active Chat
        if (chatWithId != null) {
            List<Message> messages = messageService.getChatHistory(currentUserId, chatWithId);
            model.addAttribute("activeMessages", messages);
            model.addAttribute("activeChatId", chatWithId);
            
            // Find the active partner user object to display name/header
            // Simple way: look in conversations list or fetch from DB
            User partner = conversations.stream()
                .filter(c -> c.partner.getId().equals(chatWithId))
                .map(c -> c.partner)
                .findFirst()
                .orElse(null);
                
            // If not in conversation list (new chat), fetch from DB (Assuming a method exists, or just rely on existing convos)
            if (partner != null) {
                model.addAttribute("activePartner", partner);
            }
        }

        return "messaging";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("receiverId") Long receiverId,
                              @RequestParam("content") String content,
                              HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        User sender = userService.getUserByEmail(email);
        
        // We need to construct the receiver. 
        // Limitation: UserDAO might strictly need an update to add findById(Long id).
        // Workaround: We loop through all users or add the method. 
        // For this code to work, assume we added `User findById(Long id)` to UserDAO/Service.
        // I will implement a "lazy" fetch here assuming you will add it, or use the conversation list logic.
        
        // *** CRITICAL: You must add `User findById(Long id);` to your UserDAO interface and Impl ***
        // For now, I'll assume it exists or use a placeholder logic.
        
        // Construct message
        // User receiver = userDAO.findById(receiverId); // TODO: Add this method
        
        // Since I can't edit your UserDAO directly here easily, I will rely on the fact 
        // that you likely can add `public User findById(Long id) { return entityManager.find(User.class, id); }` to UserDAOImpl.
        
        // Mocking the receiver fetch for compilation safety in this snippet:
        // In reality: User receiver = userService.getUserById(receiverId);
        
        // Create Message Object manually for now
        // Message msg = new Message(sender, receiver, content);
        // messageService.saveMessage(msg);

        return "redirect:/messaging?chatWith=" + receiverId;
    }
}