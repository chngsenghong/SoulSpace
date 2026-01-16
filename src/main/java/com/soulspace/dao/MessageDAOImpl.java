package com.soulspace.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soulspace.model.Message;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class MessageDAOImpl implements MessageDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Message message) {
        if (message.getId() == null) {
            em.persist(message);
        } else {
            em.merge(message);
        }
    }

    @Override
    public List<Message> findChat(Long userId, Long partnerId) {
        return em.createQuery(
                "FROM Message m WHERE " +
                "(m.sender.id = :u1 AND m.receiver.id = :u2) OR " +
                "(m.sender.id = :u2 AND m.receiver.id = :u1) " +
                "ORDER BY m.timestamp ASC",
                Message.class)
            .setParameter("u1", userId)
            .setParameter("u2", partnerId)
            .getResultList();
    }

    @Override
    public List<Message> findAllForUser(Long userId) {
        return em.createQuery(
                "FROM Message m WHERE m.sender.id = :uid OR m.receiver.id = :uid " +
                "ORDER BY m.timestamp DESC",
                Message.class)
            .setParameter("uid", userId)
            .getResultList();
    }
}