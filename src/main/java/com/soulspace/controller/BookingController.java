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
import jakarta.servlet.http.HttpServletResponse;

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
        session.setAttribute("role", "STUDENT");
        session.setAttribute("user", "John Student");
        session.setAttribute("email", "student@example.com");
        return "redirect:/booking";
    }

    @GetMapping("/demo/booking/professional")
    public String demoProfessionalBooking(HttpSession session) {
        session.setAttribute("userId", 2L); // Sarah Jenkins
        session.setAttribute("role", "PROFESSIONAL");
        session.setAttribute("user", "Sarah Jenkins");
        session.setAttribute("email", "pro@example.com");
        return "redirect:/booking/professional";
    }

    /* ================= STUDENT SECTION ================= */

    @GetMapping
    public String studentBooking(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("userId");
        if (studentId == null)
            return "redirect:/login";

        model.addAttribute("professionals", userService.getProfessionals());
        model.addAttribute("myAppointments", appointmentService.getAppointmentsForStudent(studentId));
        return "booking";
    }

    @PostMapping
    public String bookAppointment(
            HttpSession session,
            @RequestParam("professionalId") Long professionalId,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("type") String type) {

        Long studentId = (Long) session.getAttribute("userId");
        if (studentId == null)
            return "redirect:/login";

        User student = userService.getUserById(studentId);
        User professional = userService.getUserById(professionalId);

        Appointment appt = new Appointment();
        appt.setUser(student);
        appt.setProfessional(professional);
        appt.setAppointmentDate(LocalDate.parse(date));
        appt.setAppointmentTime(LocalTime.parse(time));
        appt.setType(Appointment.SessionType.valueOf(type.toUpperCase()));

        // When a student books, it starts as PENDING for professional to review
        appt.setStatus(Appointment.AppointmentStatus.PENDING);

        appointmentService.save(appt);
        return "redirect:/booking?success=true";
    }

    @PostMapping("/appointment/{id}/cancel")
    public String cancel(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null && appt.getUser().getId().equals(userId)) {
            appt.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentService.save(appt);
        }
        return "redirect:/booking";
    }

    @PostMapping("/appointment/{id}/reschedule")
    public String reschedule(
            @PathVariable("id") Long id,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null && appt.getUser().getId().equals(userId)) {
            appt.setAppointmentDate(LocalDate.parse(date));
            appt.setAppointmentTime(LocalTime.parse(time));
            appt.setStatus(Appointment.AppointmentStatus.PENDING); // Back to pending on change
            appointmentService.save(appt);
        }
        return "redirect:/booking?rescheduled=true";
    }

    /* ================= PROFESSIONAL SECTION ================= */

    @GetMapping("/professional")
    public String professionalView(HttpSession session, Model model) {
        Long professionalId = (Long) session.getAttribute("userId");
        if (professionalId == null)
            return "redirect:/login";

        List<Appointment> allAppointments = appointmentService.getAppointmentsByProfessional(professionalId);

        LocalDateTime now = LocalDateTime.now();

        // 1. UPCOMING: Must be (CONFIRMED or PENDING) + In the FUTURE
        List<Appointment> upcoming = allAppointments.stream()
                .filter(a -> a.getStatus() == Appointment.AppointmentStatus.CONFIRMED
                        || a.getStatus() == Appointment.AppointmentStatus.PENDING)
                .filter(a -> {
                    if (a.getAppointmentDate() == null || a.getAppointmentTime() == null)
                        return false;
                    LocalDateTime apptTime = LocalDateTime.of(a.getAppointmentDate(), a.getAppointmentTime());
                    return apptTime.isAfter(now);
                })
                .collect(Collectors.toList());
        model.addAttribute("upcomingAppointments", upcoming);

        // 2. PAST: Either COMPLETED ... OR ... (CONFIRMED but time has passed)
        List<Appointment> past = allAppointments.stream()
                .filter(a -> {
                    // Logic: Always show COMPLETED items here
                    if (a.getStatus() == Appointment.AppointmentStatus.COMPLETED)
                        return true;

                    // Logic: If CONFIRMED but missed (date passed), show here too
                    if (a.getStatus() == Appointment.AppointmentStatus.CONFIRMED) {
                        if (a.getAppointmentDate() == null || a.getAppointmentTime() == null)
                            return false;
                        LocalDateTime apptTime = LocalDateTime.of(a.getAppointmentDate(), a.getAppointmentTime());
                        return apptTime.isBefore(now) || apptTime.equals(now);
                    }
                    return false;
                })
                .collect(Collectors.toList());
        model.addAttribute("pastAppointments", past);

        return "professional-booking";
    }

    @PostMapping("/professional/appointment/{id}/confirm")
    public String confirmAppointment(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null && appt.getProfessional().getId().equals(userId)) {
            appt.setStatus(Appointment.AppointmentStatus.CONFIRMED);
            appointmentService.save(appt);
        }
        return "redirect:/booking/professional?success=true";
    }

    @PostMapping("/professional/appointment/{id}/reject")
    public String rejectAppointment(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null && appt.getProfessional().getId().equals(userId)) {
            appt.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentService.save(appt);
        }
        return "redirect:/booking/professional";
    }

    @PostMapping("/professional/appointment/{id}/cancel")
    public String professionalCancel(@PathVariable("id") Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null && appt.getProfessional().getId().equals(userId)) {
            appt.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentService.save(appt);
        }
        return "redirect:/booking/professional";
    }

    @PostMapping("/professional/appointment/{id}/reschedule")
    public String professionalReschedule(
            @PathVariable("id") Long id,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Appointment appt = appointmentService.getAppointmentById(id);
        if (appt != null && appt.getProfessional().getId().equals(userId)) {
            appt.setAppointmentDate(LocalDate.parse(date));
            appt.setAppointmentTime(LocalTime.parse(time));
            appt.setStatus(Appointment.AppointmentStatus.CONFIRMED); // Professional reschedule auto-confirms
            appointmentService.save(appt);
        }
        return "redirect:/booking/professional?success=true";
    }

    @PostMapping("/professional/book-followup")
    public void bookFollowUp(
            @RequestParam("studentId") Long studentId,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("type") String type,
            @RequestParam(value = "source", required = false) String source,
            HttpSession session,
            HttpServletResponse response) throws java.io.IOException {

        Long profId = (Long) session.getAttribute("userId");
        if (profId == null) {
            response.sendRedirect(session.getServletContext().getContextPath() + "/login");
            return;
        }

        try {
            User student = userService.getUserById(studentId);
            User professional = userService.getUserById(profId);

            Appointment appt = new Appointment();
            appt.setUser(student);
            appt.setProfessional(professional);
            appt.setAppointmentDate(LocalDate.parse(date));
            appt.setAppointmentTime(LocalTime.parse(time));
            appt.setType(Appointment.SessionType.valueOf(type.toUpperCase()));
            appt.setStatus(Appointment.AppointmentStatus.CONFIRMED); // Professional follow-up auto-confirms

            appointmentService.save(appt);

            if (source != null && source.contains("ajax")) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("OK");
                return;
            }

            String redirectUrl = session.getServletContext().getContextPath() + "/booking/professional?followup=true";
            if ("messaging".equals(source)) {
                redirectUrl = session.getServletContext().getContextPath() + "/messaging?chatWith=" + studentId
                        + "&followup=true";
            }
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    @GetMapping("/professional/appointment/{id}")
    public String professionalAppointmentDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        Appointment appt = appointmentService.getAppointmentById(id);

        if (appt != null && appt.getProfessional().getId().equals(userId)) {
            model.addAttribute("appt", appt);
            return "professional-appointment-detail";
        }
        return "redirect:/booking/professional";
    }

    // UPDATE LOGIC: This saves the notes and marks as COMPLETED
    @PostMapping("/professional/appointment/{id}/update")
    public String updateSession(
            @PathVariable("id") Long id,
            @RequestParam(value = "professionalNotes", required = false) String notes,
            @RequestParam(value = "followUpDate", required = false) String followUpDate) {

        Appointment appt = appointmentService.getAppointmentById(id);

        if (appt != null) {
            appt.setProfessionalNotes(notes);
            if (followUpDate != null && !followUpDate.trim().isEmpty()) {
                appt.setFollowUpDate(LocalDate.parse(followUpDate));
            }
            appt.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointmentService.save(appt);
        }

        return "redirect:/booking/professional?success=true";
    }
}