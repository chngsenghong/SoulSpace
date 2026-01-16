package com.soulspace.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soulspace.dao.ForumDAO;
import com.soulspace.model.Comment;
import com.soulspace.model.ForumPost;
import com.soulspace.model.PostReaction;
import com.soulspace.model.PostStatus;
import com.soulspace.model.User;

@Service
public class ForumService {

    private final ForumDAO forumDAO;
    private static final List<String> TRIGGER_KEYWORDS = Arrays.asList(
            "suicide", "kill myself", "want to die", "hurt myself", "end it all", "hopeless");

    @Autowired
    public ForumService(ForumDAO forumDAO) {
        this.forumDAO = forumDAO;
    }

     @Transactional
    public List<ForumPost> getAllPosts() {
        return forumDAO.findPostsByStatus(PostStatus.PUBLISHED);
    }

    @Transactional
    public ForumPost getPostById(Long id) {
        return forumDAO.getPostById(id);
    }

     @Transactional
    public void addPost(ForumPost post) {
        if (containsTriggerWords(post.getTitle()) || containsTriggerWords(post.getContent())) {
            post.setStatus(PostStatus.PENDING_REVIEW);
        } else {
            post.setStatus(PostStatus.PUBLISHED);
        }

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
            forumDAO.savePost(post);
        }
    }

    @Transactional
    public void addComment(Long postId, String content, User user) {
        ForumPost post = forumDAO.getPostById(postId);
        if (post != null) {
            Comment comment = new Comment(post, user, content);

            post.getComments().add(comment);

            forumDAO.saveComment(comment);
            forumDAO.savePost(post);
        }
    }

    @Transactional
    public List<ForumPost> searchPosts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllPosts();
        }
        return forumDAO.searchPosts(keyword);
    }

    @Transactional
    public ForumPost getPostAndIncrementViews(Long id) {
        ForumPost post = forumDAO.getPostById(id);
        if (post != null) {
            post.setViews(post.getViews() + 1);
            post.getReactions().size(); 
        }
        return post;
    }

    @Transactional
    public List<ForumPost> filterPosts(String keyword, String category, String sort) {
        if ((keyword == null || keyword.isEmpty()) &&
                (category == null || category.isEmpty()) &&
                (sort == null || sort.isEmpty())) {
            return getAllPosts();
        }

        return forumDAO.filterPosts(keyword, category, sort);
    }

    private boolean containsTriggerWords(String text) {
        if (text == null)
            return false;
        String lowerCaseText = text.toLowerCase();
        for (String word : TRIGGER_KEYWORDS) {
            if (lowerCaseText.contains(word)) {
                return true;
            }
        }
        return false;
    }
    @Transactional
    public List<ForumPost> getPendingPosts() {
        return forumDAO.getPendingPosts();
    }

    @Transactional
    public void approvePost(Long postId) {
        ForumPost post = forumDAO.getPostById(postId);
        if (post != null) {
            post.setStatus(PostStatus.PUBLISHED);
            forumDAO.savePost(post);
        }
    }

    @Transactional
    public void toggleSupport(Long postId, User user) {
        PostReaction existingReaction = forumDAO.findReaction(postId, user.getId());
        
        if (existingReaction != null) {
            forumDAO.removeReaction(existingReaction);
            
            ForumPost post = existingReaction.getPost();
            post.getReactions().remove(existingReaction);
        } else {
            ForumPost post = forumDAO.getPostById(postId);
            if (post != null) {
                PostReaction newReaction = new PostReaction(post, user);
                forumDAO.addReaction(newReaction);
                post.getReactions().add(newReaction); 
            }
        }
    }
    
}