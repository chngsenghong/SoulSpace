package com.soulspace.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "forum_posts")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author; 

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String category;
    private String tags; 
    private int views;
    private boolean pinned;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PostStatus status; // New field

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostReaction> reactions = new HashSet<>();


    public ForumPost() {}

    public ForumPost(User author, String title, String content, String category, List<String> tags) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category;
        
        if (tags != null && !tags.isEmpty()) {
            this.tags = String.join(",", tags);
        } else {
            this.tags = null;
        }

        this.views = 0;
        this.pinned = false;
        this.createdAt = LocalDateTime.now();
        this.status = PostStatus.PUBLISHED; 
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public String getTimeAgo() {
        if (createdAt == null) return "Just now";
        return createdAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public List<String> getTagList() {
        if (this.tags == null || this.tags.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.tags.split(","));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }

    public boolean isSupportedBy(User user) {
        if (user == null) return false;
        for (PostReaction reaction : reactions) {
            if (reaction.getUser().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    public int getSupportCount() {
        return reactions.size();
    }

    public Set<PostReaction> getReactions() { return reactions; }
    public void setReactions(Set<PostReaction> reactions) { this.reactions = reactions; }
}