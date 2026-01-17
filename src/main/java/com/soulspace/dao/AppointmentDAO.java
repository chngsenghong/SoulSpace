package com.soulspace.dao;

import java.util.List;

import com.soulspace.model.Appointment;

public interface AppointmentDAO {

    void save(Appointment appointment);

    Appointment findById(Long id);

    List<Appointment> findByStudent(Long studentId);

    List<Appointment> findByProfessional(Long professionalId);

    List<Appointment> findByProfessionalAndDate(Long professionalId, java.time.LocalDate date);
}