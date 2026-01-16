package com.soulspace.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soulspace.model.Appointment;
import com.soulspace.model.User;
import com.soulspace.service.AppointmentService;
import com.soulspace.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/booking")
public class BookingController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public BookingController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    // --- DEMO LOGINS ---
    @GetMapping("/demo/booking/student")
    public String demoStudentBooking(HttpSession session) {
        session.setAttribute("userId", 1L);
        return "redirect:/booking";
    }

    @GetMapping("/demo/booking/professional")
    public String demoProfessionalBooking(HttpSession session) {
        session.setAttribute("userId", 3L);
        return "redirect:/booking/professional";
    }

    /* ================= STUDENT SECTION ================= */
    @GetMapping
    public String studentBooking(Model model) {
        Long studentId = 1L; 
        model.addAttribute("professionals", userService.getProfessionals());
        model.addAttribute("myAppointments", appointmentService.getAppointmentsForStudent(studentId));
        return "booking";
    }

    @PostMapping
    public String bookAppointment(
        @RequestParam("professionalId") Long professionalId,
        @RequestParam("date") String date,
        @RequestParam("time") String time,
        @RequestParam("type") String type) { 

        User student = userService.getUserById(1L);
        User professional = userService.getUserById(professionalId);

        Appointment appt = new Appointment();
        appt.setUser(student);
        appt.setProfessional(professional);
        appt.setAppointmentDate(LocalDate.parse(date));
        appt.setAppointmentTime(LocalTime.parse(time));
        appt.setType(Appointment.SessionType.valueOf(type.toUpperCase()));
        appt.setStatus(Appointment.AppointmentStatus.CONFIRMED);

        appointmentService.save(appt);
        return "redirect:/booking?success=true";
    }

    @PostMapping("/appointment/{id}/cancel")
    public String cancel(@PathVariable("id") Long id) { 
        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null) {
            appt.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentService.save(appt);
        }
        return "redirect:/booking";
    }

    /* ================= PROFESSIONAL SECTION ================= */

    @GetMapping("/professional")
    public String professionalView(HttpSession session, Model model) {
        Long professionalId = (Long) session.getAttribute("userId");
        if (professionalId == null) return "redirect:/booking/demo/booking/professional";

        List<Appointment> allAppointments = appointmentService.getAppointmentsByProfessional(professionalId);
        
        LocalDateTime now = LocalDateTime.now();

        // 1. UPCOMING
        List<Appointment> upcoming = allAppointments.stream()
                .filter(a -> a.getStatus() == Appointment.AppointmentStatus.CONFIRMED)
                .filter(a -> {
                    if (a.getAppointmentDate() == null || a.getAppointmentTime() == null) return false;
                    LocalDateTime apptTime = LocalDateTime.of(a.getAppointmentDate(), a.getAppointmentTime());
                    return apptTime.isAfter(now);
                })
                .collect(Collectors.toList());
        model.addAttribute("upcomingAppointments", upcoming);

        // 2. PAST / PENDING (Include Completed)
        List<Appointment> past = allAppointments.stream()
                .filter(a -> {
                    if (a.getStatus() == Appointment.AppointmentStatus.COMPLETED) return true;
                    if (a.getStatus() == Appointment.AppointmentStatus.CONFIRMED) {
                        if (a.getAppointmentDate() == null || a.getAppointmentTime() == null) return false;
                        LocalDateTime apptTime = LocalDateTime.of(a.getAppointmentDate(), a.getAppointmentTime());
                        return apptTime.isBefore(now) || apptTime.equals(now);
                    }
                    return false;
                })
                .collect(Collectors.toList());
        model.addAttribute("pastAppointments", past);

        return "professional-booking";
    }

    @GetMapping("/professional/appointment/{id}")
    public String professionalAppointmentDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/booking/demo/booking/professional";

        Appointment appt = appointmentService.getAppointmentById(id);
        
        // Relaxed security check
        if (appt != null) {
            model.addAttribute("appt", appt);
            return "professional-appointment-detail";
        }
        return "redirect:/booking/professional";
    }

    // UPDATE LOGIC: Allow clearing date and editing notes
    @PostMapping("/professional/appointment/{id}/update")
    public String updateSession(
            @PathVariable("id") Long id,
            @RequestParam(value = "professionalNotes", required = false) String notes,
            @RequestParam(value = "followUpDate", required = false) String followUpDate) {
        
        Appointment appt = appointmentService.getAppointmentById(id);
        
        if (appt != null) {
            appt.setProfessionalNotes(notes);
            
            // FIX: Allow clearing the date if input is empty
            if (followUpDate != null && !followUpDate.trim().isEmpty()) {
                appt.setFollowUpDate(LocalDate.parse(followUpDate));
            } else {
                appt.setFollowUpDate(null); // Clear date
            }
            
            appt.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointmentService.save(appt);
        }

        return "redirect:/booking/professional";
    }

    @PostMapping("/professional/appointment/{id}/delete")
    public String deleteAppointmentProfessional(@PathVariable("id") Long id) {
        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null) {
            appt.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentService.save(appt);
        }
        return "redirect:/booking/professional";
    }
}