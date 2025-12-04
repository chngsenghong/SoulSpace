<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    // Ensure user is logged in
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - Dashboard</title>
    <style>
        /* --- GLOBAL & LAYOUT --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f9fafb;
            color: #1a1a1a;
            min-height: 100vh;
        }

        /* Sidebar Wrapper Logic */
        .main-content {
            margin-left: 16rem;
            min-height: 100vh;
            padding: 2rem;
            transition: margin-left 0.3s ease;
        }

        @media (max-width: 1024px) {
            .main-content { margin-left: 0; padding: 1rem; }
        }

        /* --- HEADER & WELCOME --- */
        .welcome-section { margin-bottom: 2rem; }
        .welcome-title { font-size: 1.875rem; font-weight: 600; color: #111827; margin-bottom: 0.5rem; }
        .welcome-subtitle { color: #6b7280; }

        /* --- STUDENT VIEW STYLES --- */
        .quick-actions { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 1rem; margin-bottom: 2.5rem; }
        .action-card { 
            background: white; padding: 1.5rem; border-radius: 1rem; border: 1px solid #e5e7eb; 
            text-decoration: none; color: inherit; display: block; transition: all 0.2s;
        }
        .action-card:hover { transform: translateY(-4px); box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1); border-color: #2563eb; }
        
        .icon-box { width: 3rem; height: 3rem; border-radius: 0.75rem; display: flex; align-items: center; justify-content: center; margin-bottom: 1rem; }
        .bg-blue { background: #eff6ff; color: #2563eb; }
        .bg-purple { background: #f3e8ff; color: #7c3aed; }
        .bg-green { background: #dcfce7; color: #059669; }
        .bg-orange { background: #ffedd5; color: #ea580c; }
        
        .card-title { font-size: 1.125rem; font-weight: 600; margin-bottom: 0.5rem; }
        .card-desc { font-size: 0.875rem; color: #6b7280; }

        /* Recommendation Cards */
        .rec-grid { display: grid; gap: 1rem; }
        @media (min-width: 768px) { .rec-grid { grid-template-columns: repeat(2, 1fr); } }
        
        .rec-card { background: white; border-radius: 1rem; padding: 1.5rem; display: flex; align-items: flex-start; gap: 1rem; border: 1px solid #e5e7eb; cursor: pointer; transition: all 0.2s; text-decoration: none; color: inherit; }
        .rec-card:hover { border-color: #2563eb; transform: translateY(-2px); }
        .rec-icon { padding: 0.5rem; background: #dbeafe; border-radius: 0.5rem; color: #2563eb; }
        .progress-bar { width: 100%; height: 0.5rem; background: #f3f4f6; border-radius: 99px; overflow: hidden; margin-top: 0.5rem; }
        .progress-fill { height: 100%; background: #2563eb; }

        /* --- PROFESSIONAL VIEW STYLES --- */
        .appt-table-container { background: white; border-radius: 1rem; overflow: hidden; border: 1px solid #e5e7eb; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .appt-table { width: 100%; border-collapse: collapse; }
        .appt-table th { background: #f9fafb; padding: 1rem; text-align: left; font-size: 0.875rem; color: #6b7280; font-weight: 600; border-bottom: 1px solid #e5e7eb; }
        .appt-table td { padding: 1rem; border-bottom: 1px solid #e5e7eb; color: #1f2937; }
        .appt-table tr:last-child td { border-bottom: none; }
        
        .status-badge { padding: 0.25rem 0.75rem; background: #dcfce7; color: #166534; border-radius: 99px; font-size: 0.75rem; font-weight: 600; }
        .btn-action { padding: 0.5rem 1rem; border: 1px solid #d1d5db; border-radius: 0.5rem; background: white; cursor: pointer; font-size: 0.875rem; transition: background 0.2s; }
        .btn-action:hover { background: #f9fafb; }

        /* Mobile Toggle */
        .mobile-menu-btn { display: none; margin-bottom: 1rem; background: none; border: none; cursor: pointer; }
        @media (max-width: 1024px) { .mobile-menu-btn { display: block; } }
    </style>
</head>
<body>

    <jsp:include page="navigation.jsp" />

    <main class="main-content">
        
        <button class="mobile-menu-btn" onclick="openSidebar()">
            <svg style="width:24px;height:24px" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
        </button>

        <div class="welcome-section">
            <h1 class="welcome-title">Welcome back, ${sessionScope.user}</h1>
            <p class="welcome-subtitle">
                <c:choose>
                    <c:when test="${sessionScope.role == 'PROFESSIONAL'}">
                        Here is your schedule for the upcoming week.
                    </c:when>
                    <c:otherwise>
                        Here's your daily mental wellness overview.
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <c:choose>
            
            <%-- VIEW 1: PROFESSIONAL DASHBOARD --%>
            <c:when test="${sessionScope.role == 'PROFESSIONAL'}">
                <div class="section-header" style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;">
                    <h2 style="font-size:1.25rem; font-weight:600;">Upcoming Client Sessions</h2>
                </div>
                
                <div class="appt-table-container">
                    <table class="appt-table">
                        <thead>
                            <tr>
                                <th>Client Name</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Type</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="appt" items="${allAppointments}">
                                <tr>
                                    <td style="font-weight: 500;">${appt.clientName}</td>
                                    <td>${appt.date}</td>
                                    <td>${appt.time}</td>
                                    <td>${appt.type}</td>
                                    <td><span class="status-badge">${appt.status}</span></td>
                                    <td>
                                        <button class="btn-action" onclick="window.location.href='${pageContext.request.contextPath}/messaging'">Message</button>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty allAppointments}">
                                <tr><td colspan="6" style="text-align:center; padding: 3rem; color:#6b7280;">No upcoming appointments found.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </c:when>

            <%-- VIEW 2: STUDENT DASHBOARD --%>
            <c:otherwise>
                <div class="quick-actions">
                    <a href="${pageContext.request.contextPath}/chatbot" class="action-card">
                        <div class="icon-box bg-blue">
                            <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z"></path></svg>
                        </div>
                        <h3 class="card-title">Talk to AI Guide</h3>
                        <p class="card-desc">Get instant support and coping strategies anytime.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/learning" class="action-card">
                        <div class="icon-box bg-purple">
                            <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>
                        </div>
                        <h3 class="card-title">Learning Hub</h3>
                        <p class="card-desc">Continue your courses and access resources.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/booking" class="action-card">
                        <div class="icon-box bg-orange">
                            <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
                        </div>
                        <h3 class="card-title">Book Therapy</h3>
                        <p class="card-desc">Schedule a session with a professional.</p>
                    </a>
                    
                    <a href="${pageContext.request.contextPath}/assessment" class="action-card">
                        <div class="icon-box bg-green">
                            <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path></svg>
                        </div>
                        <h3 class="card-title">Self-Assessment</h3>
                        <p class="card-desc">Track your progress and mental state.</p>
                    </a>
                </div>

                <div class="section-header" style="margin-bottom:1rem;">
                    <h2 style="font-size:1.25rem; font-weight:600;">Recommended for You</h2>
                </div>

                <div class="rec-grid">
                    <a href="${pageContext.request.contextPath}/learning" class="rec-card">
                        <div class="rec-icon">
                            <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path></svg>
                        </div>
                        <div style="flex:1;">
                            <h4>Understanding Anxiety Basics</h4>
                            <div style="font-size:0.875rem; color:#6b7280; margin-bottom:0.5rem;">Module 1 • 15 min remaining</div>
                            <div class="progress-bar"><div class="progress-fill" style="width: 45%;"></div></div>
                        </div>
                    </a>
                    
                    <a href="${pageContext.request.contextPath}/learning" class="rec-card">
                        <div class="rec-icon">
                            <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path></svg>
                        </div>
                        <div style="flex:1;">
                            <h4>Mindfulness Techniques</h4>
                            <div style="font-size:0.875rem; color:#6b7280; margin-bottom:0.5rem;">Daily Practice • 10 min</div>
                        </div>
                    </a>
                </div>
            </c:otherwise>
            
        </c:choose>

    </main>
</body>
</html>