package com.soulspace.service;

import com.soulspace.model.Appointment;
import java.util.List;

public interface AppointmentService {

    void save(Appointment appointment);

    Appointment getAppointmentById(Long id);

    List<Appointment> getAppointmentsForStudent(Long studentId);

    List<Appointment> getAppointmentsByProfessional(Long professionalId);
}
