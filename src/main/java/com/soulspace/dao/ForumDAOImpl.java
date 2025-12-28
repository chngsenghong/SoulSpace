package com.soulspace.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soulspace.model.ForumPost;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ForumDAOImpl implements ForumDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ForumPost> getAllPosts() {
        return entityManager.createQuery(
            "FROM ForumPost p ORDER BY p.createdAt DESC", ForumPost.class
        ).getResultList();
    }

    @Override
    public void savePost(ForumPost post) {
        if (post.getId() == null) {
            entityManager.persist(post);
        } else {
            entityManager.merge(post);
        }
    }

    @Override
    public ForumPost getPostById(Long id) {
        return entityManager.find(ForumPost.class, id);
    }

    @Override
    public void deletePost(Long id) {
        ForumPost post = entityManager.find(ForumPost.class, id);
        if (post != null) {
            entityManager.remove(post);
        }
    }
}