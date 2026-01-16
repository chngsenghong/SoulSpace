package com.soulspace.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    // Student
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Professional
    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private User professional;

    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(name = "appointment_time")
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private SessionType type;

    // Professional notes (after session)
    @Column(name = "professional_notes", length = 2000)
    private String professionalNotes;

    // Optional follow-up
    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    /* ================= ENUMS ================= */

    public enum AppointmentStatus {
        CONFIRMED,
        COMPLETED,
        CANCELLED
    }

    public enum SessionType {
        VIDEO,
        IN_PERSON
    }

    /* ================= HELPERS ================= */

    public boolean isPast() {
        return appointmentDate.isBefore(LocalDate.now());
    }

    public boolean isEditable() {
        return status == AppointmentStatus.CONFIRMED;
    }

    // --- SAFETY METHODS FOR HTML (Prevents 500 Errors) ---
    
    public String getDay() {
        return appointmentDate != null ? String.valueOf(appointmentDate.getDayOfMonth()) : "--";
    }

    public String getMonth() {
        return appointmentDate != null ? appointmentDate.getMonth().name().substring(0, 3) : "";
    }

    // Prevents crash if professional is null
    public String getProfessionalName() {
        if (professional != null) {
            return professional.getFirstName() + " " + professional.getLastName();
        }
        return "Unknown Doctor";
    }

    public String getFormattedTime() {
        return appointmentTime != null ? appointmentTime.toString() : "";
    }

    /* ================= GETTERS / SETTERS ================= */

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getProfessional() { return professional; }
    public void setProfessional(User professional) { this.professional = professional; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public SessionType getType() { return type; }
    public void setType(SessionType type) { this.type = type; }

    public String getProfessionalNotes() { return professionalNotes; }
    public void setProfessionalNotes(String professionalNotes) { this.professionalNotes = professionalNotes; }

    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
}