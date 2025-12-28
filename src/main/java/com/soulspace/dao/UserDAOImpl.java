package com.soulspace.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soulspace.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        // In JPA, persist() is for new items, merge() is for updates
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "FROM User u WHERE u.email = :email", User.class
            );
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public User loginUser(String email, String password) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "FROM User u WHERE u.email = :email AND u.password = :password", User.class
            );
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findByRole(String role) {
        TypedQuery<User> query = entityManager.createQuery(
            "FROM User u WHERE u.role = :role", User.class
        );
        query.setParameter("role", role);
        return query.getResultList();
    }
}