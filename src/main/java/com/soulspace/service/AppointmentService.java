package com.soulspace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soulspace.dao.AppointmentDAO;
import com.soulspace.model.Appointment;

@Service
public class AppointmentService {

    private final AppointmentDAO appointmentDAO;

    @Autowired
    public AppointmentService(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    @Transactional
    public void bookAppointment(Appointment appt) {
        appointmentDAO.saveAppointment(appt);
    }

    @Transactional
    public List<Appointment> getAppointmentsByUser(Long userId) {
        return appointmentDAO.getAppointmentsByUser(userId);
    }
    
    // Professionals might need to see all (or filtered) appointments
    @Transactional
    public List<Appointment> getAllAppointments() {
        // You might need to add getAllAppointments() to your DAO interface first
        // For now, returning empty or implementing based on DAO
        return appointmentDAO.getAppointmentsByUser(1L); // Placeholder if DAO method missing
    }

    @Transactional
    public Appointment getAppointmentById(Long id) {
        return appointmentDAO.findById(id);
    }
}