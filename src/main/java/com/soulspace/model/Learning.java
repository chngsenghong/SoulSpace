package com.soulspace.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "learning_modules")
public class Learning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    
    @Column(length = 1000)
    private String description;
    
    private String category;
    private String level;
    private String type;
    private String duration;
    private int lessons;
    private double rating;
    
    // --- NEW FIELD: PRICE ---
    // Added this because your HTML calls ${course.price}
    private double price;
    
    @Column(name = "student_count")
    private int studentCount;
    
    @Column(name = "link_url")
    private String linkUrl;
    
    @Column(name = "date_created")
    private LocalDate dateCreated;

    // --- AI INTEGRATION FIELDS ---
    @Column(name = "ai_summary", length = 500)
    private String aiSummary; 
    
    @Column(name = "ai_tags")
    private String aiTags; 

    // --- NEW FIELD: LEARNING POINTS ---
    // Added this because your HTML calls ${course.learningPoints}
    // ElementCollection allows storing a list of simple strings in a separate table
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "learning_points", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "point")
    private List<String> learningPoints = new ArrayList<>();

    // --- EXISTING: RELATIONSHIP TO LESSONS DATABASE TABLE ---
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id") 
    private List<LearningLesson> curriculum = new ArrayList<>();

    public Learning() {
        this.dateCreated = LocalDate.now();
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    
    public int getLessons() { return lessons; }
    public void setLessons(int lessons) { this.lessons = lessons; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    // Getter and Setter for Price
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }
    
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    
    public LocalDate getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }
    
    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }
    
    public String getAiTags() { return aiTags; }
    public void setAiTags(String aiTags) { this.aiTags = aiTags; }

    // Getter and Setter for Learning Points
    public List<String> getLearningPoints() { return learningPoints; }
    public void setLearningPoints(List<String> learningPoints) { this.learningPoints = learningPoints; }

    public List<LearningLesson> getCurriculum() { return curriculum; }
    public void setCurriculum(List<LearningLesson> curriculum) { this.curriculum = curriculum; }
}