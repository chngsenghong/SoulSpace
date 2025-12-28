package com.soulspace.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soulspace.model.Appointment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class AppointmentDAOImpl implements AppointmentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveAppointment(Appointment appointment) {
        if (appointment.getId() == null) {
            entityManager.persist(appointment);
        } else {
            entityManager.merge(appointment);
        }
    }

    @Override
    public List<Appointment> getAppointmentsByUser(Long userId) {
        TypedQuery<Appointment> query = entityManager.createQuery(
            "FROM Appointment a WHERE a.user.id = :userId ORDER BY a.appointmentDate DESC", 
            Appointment.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Appointment findById(Long id) {
        return entityManager.find(Appointment.class, id);
    }
}