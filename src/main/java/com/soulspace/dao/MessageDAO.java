package com.soulspace.dao;

import java.util.List;

import com.soulspace.model.Message;

public interface MessageDAO {

    void save(Message message);

    List<Message> findChat(Long userId, Long partnerId);

    List<Message> findAllForUser(Long userId);
}