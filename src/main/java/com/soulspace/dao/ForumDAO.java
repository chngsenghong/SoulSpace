package com.soulspace.dao;
import java.util.List;

import com.soulspace.model.Comment;
import com.soulspace.model.ForumPost;
import com.soulspace.model.PostReaction;
import com.soulspace.model.PostStatus;

public interface ForumDAO {
    List<ForumPost> getAllPosts();
    void savePost(ForumPost post);
    ForumPost getPostById(Long id);
    void deletePost(Long id);
    void saveComment(Comment comment);
    List<ForumPost> searchPosts(String keyword);
    List<ForumPost> filterPosts(String keyword, String category, String sort);
    List<ForumPost> findPostsByStatus(PostStatus status);
    List<ForumPost> getPendingPosts();
    PostReaction findReaction(Long postId, Long userId);
    void addReaction(PostReaction reaction);
    void removeReaction(PostReaction reaction);
}