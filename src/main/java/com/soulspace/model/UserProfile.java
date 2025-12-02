package com.soulspace.model;

public class UserProfile {
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String role; // e.g., "Member" or "Patient"
    private boolean emailNotifications;
    private boolean pushNotifications;

    public UserProfile(String firstName, String lastName, String email, String bio, String role, boolean emailNotifications, boolean pushNotifications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bio = bio;
        this.role = role;
        this.emailNotifications = emailNotifications;
        this.pushNotifications = pushNotifications;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public String getRole() { return role; }
    public boolean isEmailNotifications() { return emailNotifications; }
    public boolean isPushNotifications() { return pushNotifications; }
    
    // Helper for Initials (e.g., "John Doe" -> "JD")
    public String getInitials() {
        char f = (firstName != null && !firstName.isEmpty()) ? firstName.charAt(0) : '?';
        char l = (lastName != null && !lastName.isEmpty()) ? lastName.charAt(0) : '?';
        return "" + f + l;
    }
}