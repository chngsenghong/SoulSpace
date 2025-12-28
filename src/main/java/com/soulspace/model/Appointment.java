package com.soulspace.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private User professional;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String type;   
    private String status; 

    // Details
    private String meetingLink;
    private String venue;
    
    @Column(columnDefinition = "TEXT")
    private String professionalNotes;
    private LocalDate followUpDate;

    public Appointment() {}

    public Appointment(User user, User professional, String dateStr, String timeStr, String type) {
        this.user = user;
        this.professional = professional;
        this.type = type;
        this.status = "Confirmed";
        
        if (dateStr != null && !dateStr.isEmpty()) this.appointmentDate = LocalDate.parse(dateStr);
        if (timeStr != null && !timeStr.isEmpty()) this.appointmentTime = LocalTime.parse(timeStr);
        
        if ("Video".equalsIgnoreCase(type)) {
            this.meetingLink = "https://meet.soulspace.com/" + System.currentTimeMillis();
        } else {
            this.venue = "SoulSpace Clinic, Room 302";
        }
    }

    public boolean isPast() {
        if (appointmentDate == null) return false;
        return appointmentDate.isBefore(LocalDate.now());
    }

    // --- SAFETY METHODS FOR HTML (Prevents 500 Errors) ---
    
    public String getDay() {
        return appointmentDate != null ? String.valueOf(appointmentDate.getDayOfMonth()) : "--";
    }

    public String getMonth() {
        return appointmentDate != null ? appointmentDate.getMonth().name().substring(0, 3) : "";
    }

    public String getFormattedTime() {
        return appointmentTime != null ? appointmentTime.format(DateTimeFormatter.ofPattern("h:mm a")) : "--:--";
    }
    
    public String getFormattedDate() {
        return appointmentDate != null ? appointmentDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "";
    }

    // Prevents crash if professional is null
    public String getProfessionalName() {
        if (professional != null) {
            return professional.getFirstName() + " " + professional.getLastName();
        }
        return "Unknown Doctor";
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public User getProfessional() { return professional; }
    public void setProfessional(User professional) { this.professional = professional; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getProfessionalNotes() { return professionalNotes; }
    public void setProfessionalNotes(String professionalNotes) { this.professionalNotes = professionalNotes; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
}