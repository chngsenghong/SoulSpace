package com.soulspace.dao;
import java.util.List;

import com.soulspace.model.Appointment;

public interface AppointmentDAO {
    void saveAppointment(Appointment appointment);
    List<Appointment> getAppointmentsByUser(Long userId);
    Appointment findById(Long id);
}