package com.soulspace.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "post_reactions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"post_id", "user_id"}) // Ensures 1 hug per user per post
})
public class PostReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPost post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // You can extend this later with an Enum (e.g., HUG, SAME_HERE, LISTENING)
    private String reactionType; 

    public PostReaction() {}

    public PostReaction(ForumPost post, User user) {
        this.post = post;
        this.user = user;
        this.reactionType = "HUG";
    }

    public Long getId() { return id; }
    public ForumPost getPost() { return post; }
    public User getUser() { return user; }
}