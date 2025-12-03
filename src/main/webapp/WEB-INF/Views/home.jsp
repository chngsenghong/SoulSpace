<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - Dashboard</title>
    <style>
        /* --- RESET & GLOBAL --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f9fafb;
            color: #1a1a1a;
            height: 100vh;
            overflow: hidden; 
        }

        /* --- MAIN CONTENT WRAPPER (Required for Sidebar) --- */
        .main-content {
            margin-left: 16rem;
            height: 100vh;
            overflow-y: auto;
            transition: margin-left 0.3s ease;
        }

        @media (max-width: 1024px) {
            .main-content { margin-left: 0; }
        }

        .container { padding: 1rem; max-width: 80rem; margin: 0 auto; }
        @media (min-width: 1024px) { .container { padding: 2rem; } }

        /* --- HEADER & ACTIONS --- */
        .page-header { padding-top: 1rem; margin-bottom: 1.5rem; }
        .page-header h1 { font-size: 1.875rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.25rem; }
        .page-header p { color: #6b7280; }

        .quick-actions { display: grid; grid-template-columns: repeat(2, 1fr); gap: 0.75rem; margin-bottom: 1.5rem; }
        @media (min-width: 1024px) { .quick-actions { grid-template-columns: repeat(4, 1fr); } }
        
        .action-btn { height: 6rem; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 0.5rem; border: none; border-radius: 0.75rem; color: white; font-weight: 500; cursor: pointer; transition: all 0.2s; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); text-decoration: none; }
        .action-btn:hover { transform: translateY(-2px); }
        .action-btn.blue { background: #2563eb; }
        .action-btn.purple { background: #9333ea; }
        .action-btn svg { width: 1.5rem; height: 1.5rem; }

        /* --- CARDS & GRID --- */
        .two-col-grid { display: grid; gap: 1.5rem; margin-bottom: 1.5rem; }
        @media (min-width: 1024px) { .two-col-grid { grid-template-columns: 1fr 1fr; } }
        
        .card { background: white; border-radius: 0.75rem; padding: 1rem; box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1); border: 1px solid #e5e7eb; }
        .card-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 0.75rem; }
        .card-header h3 { font-size: 1.125rem; font-weight: 500; color: #1a1a1a; }
        .card-header svg { width: 1rem; height: 1rem; color: #2563eb; }
        .card.gradient { background: linear-gradient(135deg, #faf5ff 0%, #eff6ff 100%); border-color: #c4b5fd; }
        
        .info-row { display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: #6b7280; margin-top: 0.5rem; }
        .info-row svg { width: 1rem; height: 1rem; }
        
        .badge { display: inline-flex; align-items: center; padding: 0.25rem 0.75rem; border-radius: 9999px; font-size: 0.75rem; font-weight: 500; }
        .badge.yellow { background: #fef3c7; color: #92400e; }
        .badge.green { background: #d1fae5; color: #065f46; }
        .badge.outline { background: white; border: 1px solid #d1d5db; color: #374151; }
        
        .btn-outline { display: block; width: 100%; padding: 0.5rem; margin-top: 1rem; text-align: center; background: white; border: 1px solid #d1d5db; border-radius: 0.5rem; color: #1a1a1a; text-decoration: none; font-size: 0.875rem; font-weight: 500; transition: background 0.2s; cursor: pointer; }
        .btn-outline:hover { background: #f9fafb; }

        /* --- RECOMMENDATIONS --- */
        .section-header { margin-bottom: 1rem; }
        .recommendations-grid { display: grid; gap: 0.75rem; }
        @media (min-width: 1024px) { .recommendations-grid { grid-template-columns: repeat(2, 1fr); } }
        @media (min-width: 1280px) { .recommendations-grid { grid-template-columns: repeat(3, 1fr); } }
        
        .recommendation-card { background: white; border-radius: 0.75rem; padding: 1rem; border: 1px solid #e5e7eb; cursor: pointer; transition: all 0.2s; text-decoration: none; color: inherit; display: block; }
        .recommendation-card:hover { box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); transform: translateY(-2px); }
        .rec-content { display: flex; align-items: flex-start; gap: 0.75rem; }
        .rec-icon { padding: 0.5rem; background: #dbeafe; border-radius: 0.5rem; flex-shrink: 0; }
        .rec-icon svg { width: 1.25rem; height: 1.25rem; color: #2563eb; }
        .rec-details { flex: 1; }
        .rec-details h4 { font-size: 1rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.25rem; }
        .rec-meta { display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.5rem; flex-wrap: wrap; }
        .progress-bar { width: 100%; height: 0.5rem; background: #e5e7eb; border-radius: 9999px; overflow: hidden; }
        .progress-fill { height: 100%; background: #2563eb; border-radius: 9999px; }

        /* Mobile Menu Toggle */
        .mobile-menu-btn { display: none; margin-bottom: 1rem; background: none; border: none; cursor: pointer; }
        @media (max-width: 1024px) { .mobile-menu-btn { display: block; } }
    </style>
</head>
<body>

    <jsp:include page="navigation.jsp" />

    <main class="main-content">
        <div class="container">
            
            <button class="mobile-menu-btn" onclick="openSidebar()">
                <svg style="width:24px;height:24px" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
            </button>

            <div class="page-header">
                <h1>Welcome Back, ${userName}</h1>
                <p>Your mental wellness journey continues</p>
            </div>

            <div class="quick-actions">
                <a href="${pageContext.request.contextPath}/chatbot" class="action-btn blue">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" /></svg>
                    <span>AI Guide</span>
                </a>
                <a href="${pageContext.request.contextPath}/assessment" class="action-btn purple">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" /></svg>
                    <span>Self-Assess</span>
                </a>
                <a href="${pageContext.request.contextPath}/booking" class="action-btn blue">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                    <span>Book Session</span>
                </a>
                <a href="${pageContext.request.contextPath}/learning" class="action-btn purple">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" /></svg>
                    <span>Learn</span>
                </a>
            </div>

            <div class="two-col-grid">
                <div class="card">
                    <div class="card-header">
                        <h3>Upcoming Appointment</h3>
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                    </div>
                    <div class="card-content">
                        <div>
                            <p style="color: #1a1a1a; font-weight: 500;">${appointment.doctorName}</p>
                            <p style="font-size: 0.875rem; color: #6b7280;">${appointment.doctorTitle}</p>
                        </div>
                        <div class="info-row">
                            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                            <span>${appointment.date}</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/booking" class="btn-outline">View Details</a>
                    </div>
                </div>

                <div class="card gradient">
                    <div class="card-header">
                        <h3>Latest Assessment</h3>
                    </div>
                    <div class="card-content">
                        <div style="display: flex; align-items: center; justify-content: space-between;">
                            <span style="color: #374151;">${assessment.title}</span>
                            <span class="badge ${assessment.colorClass}">${assessment.result}</span>
                        </div>
                        <p style="font-size: 0.875rem; color: #6b7280; margin-top: 0.5rem;">${assessment.date}</p>
                        <a href="${pageContext.request.contextPath}/assessment" class="btn-outline">Take New Assessment</a>
                    </div>
                </div>
            </div>

            <div class="section-header">
                <h2>Recommended for You</h2>
            </div>
            <div class="recommendations-grid">
                
                <c:forEach var="rec" items="${recommendations}">
                    <a href="${pageContext.request.contextPath}/learning" class="recommendation-card">
                        <div class="rec-content">
                            <div class="rec-icon">
                                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                    <c:choose>
                                        <c:when test="${rec.iconType == 'meditation'}">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                        </c:when>
                                        <c:otherwise>
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                                        </c:otherwise>
                                    </c:choose>
                                </svg>
                            </div>
                            <div class="rec-details">
                                <h4>${rec.title}</h4>
                                <div class="rec-meta">
                                    <span class="badge outline">${rec.type}</span>
                                    <span>${rec.duration}</span>
                                </div>
                                
                                <c:if test="${rec.progress > 0}">
                                    <div class="progress-bar">
                                        <div class="progress-fill" style="width: ${rec.progress}%;"></div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </a>
                </c:forEach>

            </div>
        </div>
    </main>

    <script>
        // No extra JS needed for navigation as we use standard <a> tags now
    </script>
</body>
</html>