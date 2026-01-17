package com.soulspace.service;

import java.time.LocalDate;
import java.util.List;

import com.soulspace.model.Appointment;

public interface AppointmentService {

    void save(Appointment appointment);

    Appointment getAppointmentById(Long id);

    List<Appointment> getAppointmentsForStudent(Long studentId);

    List<Appointment> getAppointmentsByProfessional(Long professionalId);
    
    List<Appointment> getBookedAppointments(Long professionalId, LocalDate date);
}
