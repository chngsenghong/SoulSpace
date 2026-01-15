package com.soulspace.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soulspace.model.Comment;
import com.soulspace.model.ForumPost;
import com.soulspace.model.PostReaction;
import com.soulspace.model.PostStatus;

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

    @Override
    public void saveComment(Comment comment) {
        if (comment.getId() == null) {
            entityManager.persist(comment);
        } else {
            entityManager.merge(comment);
        }
    }

    @Override
    public List<ForumPost> searchPosts(String keyword) {
    // JPQL Query to search title, content, or category (case-insensitive)
        String jpql = "FROM ForumPost p WHERE LOWER(p.title) LIKE :key " +
                    "OR LOWER(p.content) LIKE :key " +
                    "OR LOWER(p.category) LIKE :key " +
                    "ORDER BY p.createdAt DESC";
                    
        return entityManager.createQuery(jpql, ForumPost.class)
                .setParameter("key", "%" + keyword.toLowerCase() + "%") // Add % for wildcard search
                .getResultList();
    }

    @Override
    public List<ForumPost> filterPosts(String keyword, String category, String sort) {
        StringBuilder jpql = new StringBuilder("FROM ForumPost p WHERE 1=1");
        
        // 1. Dynamic Filtering
        if (keyword != null && !keyword.isEmpty()) {
            jpql.append(" AND (LOWER(p.title) LIKE :key OR LOWER(p.content) LIKE :key)");
        }
        if (category != null && !category.isEmpty()) {
            jpql.append(" AND p.category = :category");
        }

        // 2. Dynamic Sorting
        if ("popular".equals(sort)) {
            jpql.append(" ORDER BY size(p.comments) DESC, p.views DESC"); // Most discussed first
        } else if ("oldest".equals(sort)) {
            jpql.append(" ORDER BY p.createdAt ASC");
        } else {
            jpql.append(" ORDER BY p.createdAt DESC"); // Default: Newest
        }

        var query = entityManager.createQuery(jpql.toString(), ForumPost.class);

        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("key", "%" + keyword.toLowerCase() + "%");
        }
        if (category != null && !category.isEmpty()) {
            query.setParameter("category", category);
        }
        
        return query.getResultList();
    }

    @Override
    public List<ForumPost> findPostsByStatus(PostStatus status) {
        return entityManager.createQuery(
            "FROM ForumPost p WHERE p.status = :status ORDER BY p.createdAt DESC", ForumPost.class)
            .setParameter("status", status)
            .getResultList();
    }

    @Override
    public List<ForumPost> getPendingPosts() {
        return entityManager.createQuery(
            "FROM ForumPost p WHERE p.status = 'PENDING_REVIEW' ORDER BY p.createdAt ASC", 
            ForumPost.class
        ).getResultList();
    }

    @Override
    public PostReaction findReaction(Long postId, Long userId) {
        try {
            return entityManager.createQuery(
                "FROM PostReaction r WHERE r.post.id = :postId AND r.user.id = :userId", 
                PostReaction.class)
                .setParameter("postId", postId)
                .setParameter("userId", userId)
                .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public void addReaction(PostReaction reaction) {
        entityManager.persist(reaction);
    }

    @Override
    public void removeReaction(PostReaction reaction) {
        entityManager.remove(reaction);
    }
}