package com.soulspace.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soulspace.model.Appointment;
import com.soulspace.model.Assessment;
import com.soulspace.model.Recommendation;
import com.soulspace.service.AppointmentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final AppointmentService appointmentService;

    @Autowired
    public DashboardController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String showDashboard(HttpSession session, Model model) {
        // 1. Check Login
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        Long userId = (Long) session.getAttribute("userId");

        // 2. Logic for Professionals
        if ("PROFESSIONAL".equals(role)) {
            // Professionals see all appointments (or specific logic)
            // Ensure getAllAppointments exists in Service/DAO if used here
             model.addAttribute("allAppointments", appointmentService.getAppointmentsByUser(userId));
        } 
        
        // 3. Logic for Students (Default)
        else {
            if (userId != null) {
                List<Appointment> apps = appointmentService.getAppointmentsByUser(userId);
                // Simple logic: Take the first one as "Next Session"
                if (!apps.isEmpty()) {
                    model.addAttribute("nextAppointment", apps.get(0));
                }
            }

            // Mock Assessment Data (Database table not created yet)
            model.addAttribute("assessment", new Assessment(
                "Stress Level Assessment", 
                "Moderate", 
                "yellow", 
                "Nov 3, 2025"
            ));
            
            // Dynamic Recommendations (Standardized List)
            List<Recommendation> recs = new ArrayList<>();
            recs.add(new Recommendation("Understanding Anxiety", "Learning Module", "15 min", 45, "brain"));
            recs.add(new Recommendation("Morning Meditation", "Exercise", "10 min", 0, "meditation"));
            recs.add(new Recommendation("Sleep Hygiene", "Article", "5 min", 100, "brain"));
            
            model.addAttribute("recommendations", recs);
        }

        return "dashboard";
    }
}