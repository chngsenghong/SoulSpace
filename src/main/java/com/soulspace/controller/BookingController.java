package com.soulspace.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    public BookingController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @GetMapping
    public String showBookingPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        // 1. Fetch Professionals Safely
        List<User> professionals = new ArrayList<>();
        try {
            professionals = userService.getProfessionals();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("professionals", professionals);

        // 2. Fetch My Appointments Safely
        List<Appointment> myAppointments = new ArrayList<>();
        try {
            myAppointments = appointmentService.getAppointmentsByUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("myAppointments", myAppointments);

        return "booking";
    }

    @PostMapping
    public String bookAppointment(
        @RequestParam("professionalId") Long professionalId, // FIX: Use ID from form
        @RequestParam("date") String date,
        @RequestParam("time") String time,
        @RequestParam("type") String type,
        HttpSession session) 
    {
        String email = (String) session.getAttribute("email");
        if (email == null) return "redirect:/login";

        try {
            // 1. Get Logged-in Student
            User student = userService.getUserByEmail(email);
            
            // 2. Get Selected Professional (User Object)
            User professional = userService.getUserById(professionalId);

            // 3. Create & Save Appointment
            if (student != null && professional != null) {
                Appointment newAppt = new Appointment(student, professional, date, time, type);
                appointmentService.bookAppointment(newAppt);
                return "redirect:/booking?success=true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "redirect:/booking?error=true";
    }
}