package com.soulspace.controller;

import java.io.IOException;
import java.util.UUID;

import com.soulspace.data.AppointmentData;
import com.soulspace.model.Appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Get Data from Form
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String type = request.getParameter("type");
        String professional = "Dr. Sarah Johnson"; // Hardcoded for this example, or from hidden field

        // 2. Get Current Student Name
        HttpSession session = request.getSession();
        String studentName = (String) session.getAttribute("user");
        if(studentName == null) studentName = "Guest Student";

        // 3. Save to Shared Data
        Appointment newAppt = new Appointment(
            UUID.randomUUID().toString(),
            studentName,
            professional,
            date,
            time,
            type
        );
        AppointmentData.addAppointment(newAppt);

        // 4. Redirect with success flag
        response.sendRedirect("booking?success=true");
    }
}