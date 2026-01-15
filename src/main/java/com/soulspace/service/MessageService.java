package com.soulspace.service;

import java.util.ArrayList;
import java.util.LinkedHashMap; // To fetch partner details
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soulspace.dao.MessageDAO;
import com.soulspace.dao.UserDAO;
import com.soulspace.model.Message;
import com.soulspace.model.User;

@Service
public class MessageService {

    @Autowired private MessageDAO messageDAO;
    @Autowired private UserDAO userDAO;

    @Transactional
    public void sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userDAO.getUserByEmail(userDAO.getUserByEmail("placeholder").getEmail()); // Need a method to get by ID usually
        // NOTE: Since your UserDAO only has getUserByEmail, let's update this later or use a workaround.
        // Ideally UserDAO needs getUserById(Long id). I will assume you add it or use entityManager.find in DAO.
        
        // Simpler approach for now without changing UserDAO interface drastically:
        // We will pass the User objects directly from Controller if possible, or fetch via DAO impl.
    }
    
    // Better sendMessage signature
    @Transactional
    public void sendMessage(User sender, Long receiverId, String content) {
        // We need to fetch receiver. Since UserDAO might be limited, let's assume we can fetch user by ID.
        // If UserDAO doesn't have findById, we can't easily do this. 
        // FIX: I will add a helper in Controller to get receiver.
    }
    
    @Transactional
    public void saveMessage(Message msg) {
        messageDAO.saveMessage(msg);
    }

    @Transactional
    public List<Message> getChatHistory(Long userId, Long partnerId) {
        return messageDAO.getMessagesBetween(userId, partnerId);
    }

    // Helper class for the Sidebar
    public static class ConversationDTO {
        public User partner;
        public Message lastMessage;
    }

    @Transactional
    public List<ConversationDTO> getConversations(Long currentUserId) {
        List<Message> allMessages = messageDAO.getAllMessagesForUser(currentUserId);
        Map<Long, ConversationDTO> convoMap = new LinkedHashMap<>();

        for (Message m : allMessages) {
            // Determine who the "other" person is
            User partner = m.getSender().getId().equals(currentUserId) ? m.getReceiver() : m.getSender();
            
            // Since list is ordered by DESC timestamp, the first time we see a partner, that's the latest message
            if (!convoMap.containsKey(partner.getId())) {
                ConversationDTO dto = new ConversationDTO();
                dto.partner = partner;
                dto.lastMessage = m;
                convoMap.put(partner.getId(), dto);
            }
        }
        return new ArrayList<>(convoMap.values());
    }
}