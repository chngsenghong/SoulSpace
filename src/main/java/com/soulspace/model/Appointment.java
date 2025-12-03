package com.soulspace.model;

public class Appointment {
    private String doctorName;
    private String doctorTitle;
    private String date;

    public Appointment(String doctorName, String doctorTitle, String date) {
        this.doctorName = doctorName;
        this.doctorTitle = doctorTitle;
        this.date = date;
    }

    public String getDoctorName() { return doctorName; }
    public String getDoctorTitle() { return doctorTitle; }
    public String getDate() { return date; }
}