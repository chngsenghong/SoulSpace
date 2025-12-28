package com.soulspace.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soulspace.model.Message;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class MessageDAOImpl implements MessageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveMessage(Message message) {
        if (message.getId() == null) {
            entityManager.persist(message);
        } else {
            entityManager.merge(message);
        }
    }

    @Override
    public List<Message> getMessagesBetween(Long userId1, Long userId2) {
        // Fetch chat history between two specific people
        String hql = "FROM Message m WHERE (m.sender.id = :u1 AND m.receiver.id = :u2) " +
                     "OR (m.sender.id = :u2 AND m.receiver.id = :u1) ORDER BY m.timestamp ASC";
        TypedQuery<Message> query = entityManager.createQuery(hql, Message.class);
        query.setParameter("u1", userId1);
        query.setParameter("u2", userId2);
        return query.getResultList();
    }

    @Override
    public List<Message> getAllMessagesForUser(Long userId) {
        // Fetch ALL messages for this user to calculate who they talked to
        String hql = "FROM Message m WHERE m.sender.id = :uid OR m.receiver.id = :uid ORDER BY m.timestamp DESC";
        TypedQuery<Message> query = entityManager.createQuery(hql, Message.class);
        query.setParameter("uid", userId);
        return query.getResultList();
    }
}