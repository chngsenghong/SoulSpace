package com.soulspace.data;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.soulspace.model.Comment;
import com.soulspace.model.ForumPost;

public class ForumData {
    private static List<ForumPost> posts = new CopyOnWriteArrayList<>();
    private static int idCounter = 100;

    // Static block to initialize data once
    static {
        // Post 1 (Pinned)
        ForumPost p1 = new ForumPost(1, "Welcome to the SoulSpace Community Forum!",
            "We're thrilled to have you here. This is a safe space for sharing experiences.",
            "Admin", "AD", "#2563eb", "General", Arrays.asList("Announcement"), 2, 1200, "2 days ago", true);
        p1.addComment(new Comment("John Doe", "JD", "#10b981", "Thank you! Excited to be here.", "1 day ago"));
        p1.addComment(new Comment("Sarah M.", "SM", "#9333ea", "Great initiative.", "5 hours ago"));
        posts.add(p1);

        // Post 2
        ForumPost p2 = new ForumPost(2, "How I overcame my anxiety through mindfulness",
            "I wanted to share my journey of how practicing mindfulness changed my life...",
            "Sarah M.", "SM", "#9333ea", "Anxiety", Arrays.asList("Anxiety", "Success"), 5, 345, "3 hours ago", false);
        p2.addComment(new Comment("Emily R.", "ER", "#f59e0b", "This is so inspiring, thanks for sharing!", "1 hour ago"));
        posts.add(p2);
        
        // Post 3
        posts.add(new ForumPost(3, "Seeking advice on managing work stress",
            "I've been feeling overwhelmed with work lately. Any tips?",
            "John D.", "JD", "#10b981", "Stress", Arrays.asList("Stress", "Advice"), 0, 189, "5 hours ago", false));
        
            addPost(new ForumPost(1, "Welcome to the SoulSpace Community Forum!",
            "We're thrilled to have you here...", "Admin", "AD", "#2563eb", "General", 
            Arrays.asList("Announcement"), 0, 1200, "2 days ago", true));
    }

    public static List<ForumPost> getAllPosts() { return posts; }

   public static ForumPost getPostById(int id) {
        return posts.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    // --- CRUD METHODS ---

    // CREATE
    public static void addPost(ForumPost post) {
        // Assign a new ID if it doesn't have one (or simple auto-increment logic)
        if (post.getId() == 0) {
            // This is a hacky way to set ID since the field is private/final in the previous model. 
            // Ideally, pass the ID in the constructor in the Servlet.
        }
        posts.add(0, post); // Add to top
    }
    
    // Helper to generate next ID
    public static int getNextId() {
        return ++idCounter;
    }

    // DELETE
    public static void deletePost(int id) {
        posts.removeIf(p -> p.getId() == id);
    }

    // UPDATE
    public static void updatePost(int id, String title, String category, String content) {
        for (ForumPost p : posts) {
            if (p.getId() == id) {
                // In a real app, use Setters. 
                // Since your model fields might be private without setters, 
                // we might need to replace the object or add setters to ForumPost.java.
                // Assuming setters exist:
                p.setTitle(title);
                p.setCategory(category);
                p.setExcerpt(content); // Using excerpt as content for this mock
                break;
            }
        }
    }

}