package com.soulspace.dao;

import java.time.LocalDate;
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
    public void save(Appointment appointment) {
        if (appointment.getId() == null) {
            entityManager.persist(appointment);
        } else {
            entityManager.merge(appointment);
        }
    }

    @Override
    public Appointment findById(Long id) {
        return entityManager.find(Appointment.class, id);
    }

    @Override
    public List<Appointment> findByStudent(Long studentId) {
        String hql = """
            FROM Appointment a
            WHERE a.user.id = :studentId
            ORDER BY a.appointmentDate DESC, a.appointmentTime DESC
        """;

        TypedQuery<Appointment> query =
                entityManager.createQuery(hql, Appointment.class);
        query.setParameter("studentId", studentId);

        return query.getResultList();
    }
    @Override
    public List<Appointment> findByProfessional(Long professionalId) {
        String hql = """
            FROM Appointment a
            WHERE a.professional.id = :professionalId
            ORDER BY a.appointmentDate DESC, a.appointmentTime DESC
        """;

        TypedQuery<Appointment> query = entityManager.createQuery(hql, Appointment.class);
        query.setParameter("professionalId", professionalId);
        return query.getResultList();
    }

    @Override
    public List<Appointment> findByProfessionalAndDate(Long professionalId, LocalDate date) {
        String hql = """
            FROM Appointment a
            WHERE a.professional.id = :pid 
            AND a.appointmentDate = :date
            AND a.status != 'CANCELLED'
        """;
        
        TypedQuery<Appointment> query = entityManager.createQuery(hql, Appointment.class);
        query.setParameter("pid", professionalId);
        query.setParameter("date", date);
        
        return query.getResultList();
    }
}