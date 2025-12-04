package com.soulspace.data;

import com.soulspace.model.Appointment;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppointmentData {
    private static List<Appointment> appointments = new CopyOnWriteArrayList<>();

    // Add some dummy data for testing
    static {
        appointments.add(new Appointment("1", "John Doe", "Dr. Sarah Johnson", "2025-12-08", "14:00", "Video Call"));
    }

    public static List<Appointment> getAllAppointments() {
        return appointments;
    }

    public static void addAppointment(Appointment appt) {
        appointments.add(0, appt); // Add to top
    }
}