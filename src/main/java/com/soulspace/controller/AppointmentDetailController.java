package com.soulspace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soulspace.model.Appointment;
import com.soulspace.service.AppointmentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/appointment")
public class AppointmentDetailController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/{id}")
    public String showAppointmentDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Appointment appt = appointmentService.getAppointmentById(id);

        // Security check: Ensure the logged-in user owns this appointment
        if (appt == null || (!appt.getUser().getId().equals(userId) && !appt.getProfessional().getId().equals(userId))) {
            return "redirect:/dashboard";
        }

        model.addAttribute("appt", appt);
        return "appointment-detail";
    }
}