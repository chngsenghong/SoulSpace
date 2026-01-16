package com.soulspace.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soulspace.dao.MessageDAO;
import com.soulspace.dao.UserDAO;
import com.soulspace.model.Message;
import com.soulspace.model.User;

@Service
@Transactional
public class MessageService {

    private final MessageDAO messageDAO;
    private final UserDAO userDAO;

    public MessageService(MessageDAO messageDAO, UserDAO userDAO) {
        this.messageDAO = messageDAO;
        this.userDAO = userDAO;
    }

    public void sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userDAO.findById(senderId);
        User receiver = userDAO.findById(receiverId);

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content);
        msg.setConversationStatus("ACTIVE");

        messageDAO.save(msg);
    }

    public List<Message> getChat(Long userId, Long partnerId) {
        return messageDAO.findChat(userId, partnerId);
    }

    public Map<User, Message> getActiveConversations(Long userId) {
        List<Message> all = messageDAO.findAllForUser(userId);
        Map<User, Message> map = new LinkedHashMap<>();

        for (Message m : all) {
            User partner = m.getSender().getId().equals(userId) ? m.getReceiver() : m.getSender();
            if ("ACTIVE".equals(m.getConversationStatus())) {
                map.putIfAbsent(partner, m);
            }
        }
        return map;
    }

    public Map<User, Message> getArchivedConversations(Long userId) {
        List<Message> all = messageDAO.findAllForUser(userId);
        Map<User, Message> map = new LinkedHashMap<>();

        for (Message m : all) {
            User partner = m.getSender().getId().equals(userId) ? m.getReceiver() : m.getSender();
            if ("ARCHIVED".equals(m.getConversationStatus())) {
                map.putIfAbsent(partner, m);
            }
        }
        return map;
    }

    public void archiveChat(Long userId, Long partnerId) {
        List<Message> chat = messageDAO.findChat(userId, partnerId);
        for (Message m : chat) {
            m.setConversationStatus("ARCHIVED");
            messageDAO.save(m);
        }
    }

    public void unarchiveChat(Long userId, Long partnerId) {
        List<Message> chat = messageDAO.findChat(userId, partnerId);
        for (Message m : chat) {
            m.setConversationStatus("ACTIVE");
            messageDAO.save(m);
        }
    }
}