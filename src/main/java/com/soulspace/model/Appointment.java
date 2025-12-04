package com.soulspace.model;

public class Appointment {
    private String id;
    private String clientName;
    private String professionalName;
    private String date;
    private String time;
    private String type; // Video, In-Person
    private String status;

    public Appointment(String id, String clientName, String professionalName, String date, String time, String type) {
        this.id = id;
        this.clientName = clientName;
        this.professionalName = professionalName;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = "Confirmed";
    }

    // Getters
    public String getId() { return id; }
    public String getClientName() { return clientName; }
    public String getProfessionalName() { return professionalName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public String getStatus() { return status; }
}