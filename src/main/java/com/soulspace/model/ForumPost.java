package com.soulspace.model;

import java.util.ArrayList;
import java.util.List; 

public class ForumPost {
    private int id;
    private String title;
    private String excerpt;
    private String authorName;
    private String authorInitials;
    private String authorColor;
    private String category;
    private List<String> tags;
    private int likes;
    private int replies;
    private int views;
    private String timeAgo; 
    private boolean isPinned;
    private boolean isLikedByCurrentUser;
    private boolean isBookmarked;
    private List<Comment> comments;

    // Constructor
    public ForumPost(int id, String title, String excerpt, String authorName, 
                     String authorInitials, String authorColor, String category, 
                     List<String> tags, int replies, int views, String timeAgo, 
                     boolean isPinned) {
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
        this.authorName = authorName;
        this.authorInitials = authorInitials;
        this.authorColor = authorColor;
        this.category = category;
        this.tags = tags;
        this.replies = replies;
        this.views = views;
        this.timeAgo = timeAgo;
        this.isPinned = isPinned;
        this.likes = 0; // Default
        this.isLikedByCurrentUser = false;
        this.isBookmarked = false;
        this.comments = new ArrayList<>();
    }
    
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getExcerpt() { return excerpt; }
    public String getAuthorName() { return authorName; }
    public String getAuthorInitials() { return authorInitials; }
    public String getAuthorColor() { return authorColor; }
    public String getCategory() { return category; }
    public List<String> getTags() { return tags; }
    public int getReplies() { return replies; }
    public int getViews() { return views; }
    public String getTimeAgo() { return timeAgo; }
    public boolean isPinned() { return isPinned; }
    public boolean isLikedByCurrentUser() { return isLikedByCurrentUser; }
    public void setLikedByCurrentUser(boolean likedByCurrentUser) { isLikedByCurrentUser = likedByCurrentUser; }
    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
    public List<Comment> getComments() { return comments; }
    public void addComment(Comment comment) { this.comments.add(comment); }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
}