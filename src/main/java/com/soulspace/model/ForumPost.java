package com.soulspace.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // 1. No-Arg Constructor (REQUIRED for Hibernate)
    public ForumPost() {}

    // 2. Parameterized Constructor (REQUIRED for Controller)
    // Note: We don't pass ID (auto-generated) or Date (auto-generated)
    public ForumPost(User author, String title, String content, String category, String tags) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.views = 0;
        this.pinned = false;
        this.createdAt = LocalDateTime.now(); // Set time immediately
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public String getTimeAgo() {
        if (createdAt == null) return "Just now";
        return createdAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
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
}