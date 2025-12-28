package com.soulspace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soulspace.dao.ForumDAO;
import com.soulspace.model.ForumPost;

@Service
public class ForumService {

    private final ForumDAO forumDAO;

    @Autowired
    public ForumService(ForumDAO forumDAO) {
        this.forumDAO = forumDAO;
    }

    @Transactional
    public List<ForumPost> getAllPosts() {
        return forumDAO.getAllPosts();
    }

    @Transactional
    public ForumPost getPostById(Long id) {
        return forumDAO.getPostById(id);
    }

    @Transactional
    public void addPost(ForumPost post) {
        forumDAO.savePost(post);
    }

    @Transactional
    public void deletePost(Long id) {
        forumDAO.deletePost(id);
    }

    @Transactional
    public void updatePost(Long id, String title, String category, String content) {
        ForumPost post = forumDAO.getPostById(id);
        if (post != null) {
            post.setTitle(title);
            post.setCategory(category);
            post.setContent(content);
            // Hibernate will automatically save changes to 'post' at the end of the transaction
            forumDAO.savePost(post); 
        }
    }
}