package com.soulspace.dao;

import java.util.List;

import com.soulspace.model.Message;

public interface MessageDAO {
    void saveMessage(Message message);
    List<Message> getMessagesBetween(Long userId1, Long userId2);
    // Gets all messages involving the user (to build the sidebar list)
    List<Message> getAllMessagesForUser(Long userId); 
}