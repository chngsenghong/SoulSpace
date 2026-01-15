package com.soulspace.service;

import com.soulspace.dao.AppointmentDAO;
import com.soulspace.model.Appointment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDAO appointmentDAO;

    public AppointmentServiceImpl(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public void save(Appointment appointment) {
        appointmentDAO.save(appointment);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentDAO.findById(id);
    }

    @Override
    public List<Appointment> getAppointmentsForStudent(Long studentId) {
        return appointmentDAO.findByStudent(studentId);
    }

    @Override
    public List<Appointment> getAppointmentsByProfessional(Long professionalId) {
        return appointmentDAO.findByProfessional(professionalId);
    }
}