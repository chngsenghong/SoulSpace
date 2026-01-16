package com.soulspace.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student_enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Learning course;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "progress_percent")
    private int progressPercent;

    public Enrollment() {
        this.enrollmentDate = LocalDate.now();
        this.progressPercent = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Learning getCourse() { return course; }
    public void setCourse(Learning course) { this.course = course; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
}