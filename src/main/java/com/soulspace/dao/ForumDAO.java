package com.soulspace.dao;
import java.util.List;

import com.soulspace.model.ForumPost;

public interface ForumDAO {
    List<ForumPost> getAllPosts();
    void savePost(ForumPost post);
    ForumPost getPostById(Long id);
    void deletePost(Long id);
}