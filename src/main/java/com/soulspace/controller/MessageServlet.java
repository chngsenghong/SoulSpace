package com.soulspace.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles routing to the Secure Messaging UI (Module 8).
 * This servlet ONLY loads the view — no backend logic yet.
 */
@WebServlet("/messaging")
public class MessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forward to your messages.html SPA (located under /WEB-INF/Views/)
        request.getRequestDispatcher("/WEB-INF/views/messaging.jsp")
               .forward(request, response);
    }
}