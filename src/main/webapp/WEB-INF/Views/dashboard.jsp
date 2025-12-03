<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
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
        /* --- GLOBAL & LAYOUT (Matches Chatbot) --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        
        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; 
            background: #f9fafb; 
            min-height: 100vh;
            display: flex; /* Sidebar + Content Layout */
        }

        /* --- SIDEBAR STYLES (Exact copy from Chatbot) --- */
        .sidebar {
            width: 16rem;
            background: white;
            border-right: 1px solid #e5e7eb;
            display: flex;
            flex-direction: column;
            position: fixed; /* Fixed position */
            top: 0;
            left: 0;
            height: 100vh;
            z-index: 50;
        }

        .logo-container {
            padding: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            border-bottom: 1px solid #f3f4f6;
        }

        .logo-icon {
            width: 2rem;
            height: 2rem;
            background: linear-gradient(135deg, #2563eb 0%, #7c3aed 100%);
            border-radius: 0.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
        }

        .logo-text {
            font-size: 1.25rem;
            font-weight: 600;
            background: linear-gradient(135deg, #1a1a1a 0%, #4b5563 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .nav-links {
            padding: 1.5rem 1rem;
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
            flex: 1;
        }

        .nav-item {
            display: flex;
            align-items: center;
            gap: 0.75rem;
            padding: 0.75rem 1rem;
            text-decoration: none;
            color: #4b5563;
            border-radius: 0.5rem;
            transition: all 0.2s;
            font-weight: 500;
        }

        .nav-item:hover {
            background: #f3f4f6;
            color: #1a1a1a;
        }

        /* Active State Style */
        .nav-item.active {
            background: #eff6ff;
            color: #2563eb;
        }

        .user-profile {
            padding: 1rem;
            border-top: 1px solid #f3f4f6;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .avatar {
            width: 2.5rem;
            height: 2.5rem;
            background: #e5e7eb;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            color: #4b5563;
        }

        .user-info {
            display: flex;
            flex-direction: column;
        }

        .user-name {
            font-size: 0.875rem;
            font-weight: 500;
            color: #1a1a1a;
        }

        .user-role {
            font-size: 0.75rem;
            color: #6b7280;
        }

        /* --- DASHBOARD CONTENT --- */
        .main-content {
            margin-left: 16rem; /* Push content to right */
            width: calc(100% - 16rem);
            min-height: 100vh;
            padding: 2rem;
        }

        @media (max-width: 1024px) {
            .sidebar { display: none; }
            .main-content { margin-left: 0; width: 100%; }
        }

        /* --- DASHBOARD SPECIFIC COMPONENTS --- */
        .welcome-section { margin-bottom: 2rem; }
        .welcome-title { font-size: 1.875rem; font-weight: 600; color: #111827; margin-bottom: 0.5rem; }
        .welcome-subtitle { color: #6b7280; }

        .quick-actions { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 1.5rem; margin-bottom: 2.5rem; }
        .action-card { background: white; padding: 1.5rem; border-radius: 1rem; border: 1px solid #e5e7eb; transition: all 0.2s; cursor: pointer; text-decoration: none; color: inherit; display: block; }
        .action-card:hover { transform: translateY(-4px); box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1); border-color: #2563eb; }
        .icon-box { width: 3rem; height: 3rem; border-radius: 0.75rem; display: flex; align-items: center; justify-content: center; margin-bottom: 1rem; }
        .card-title { font-size: 1.125rem; font-weight: 600; margin-bottom: 0.5rem; }
        .card-desc { font-size: 0.875rem; color: #6b7280; }

        .bg-blue { background: #eff6ff; color: #2563eb; }
        .bg-purple { background: #f3e8ff; color: #7c3aed; }
        .bg-green { background: #dcfce7; color: #059669; }
        .bg-orange { background: #ffedd5; color: #ea580c; }

        .section-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.5rem; }
        .section-title { font-size: 1.25rem; font-weight: 600; color: #111827; }

        .rec-card { background: white; border-radius: 1rem; padding: 1.5rem; display: flex; align-items: center; gap: 1.5rem; border: 1px solid #e5e7eb; margin-bottom: 1rem; cursor: pointer; transition: all 0.2s; }
        .rec-card:hover { border-color: #2563eb; }
        .rec-icon { width: 4rem; height: 4rem; background: #eff6ff; border-radius: 0.75rem; display: flex; align-items: center; justify-content: center; color: #2563eb; flex-shrink: 0; }
        .rec-details h4 { font-size: 1rem; font-weight: 600; margin-bottom: 0.25rem; }
        .rec-meta { display: flex; gap: 1rem; font-size: 0.875rem; color: #6b7280; margin-bottom: 0.5rem; }
        .progress-bar { width: 100%; height: 0.5rem; background: #f3f4f6; border-radius: 9999px; overflow: hidden; }
        .progress-fill { height: 100%; background: #2563eb; border-radius: 9999px; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="logo-container">
            <div class="logo-icon">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="24" height="24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
            </div>
            <span class="logo-text">SoulSpace</span>
        </div>

        <nav class="nav-links">
            <a href="dashboard" class="nav-item active">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="20" height="20">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
                Dashboard
            </a>
            
            <a href="chatbot" class="nav-item">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="20" height="20">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                </svg>
                AI Guide
            </a>
            
            <a href="learning" class="nav-item">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="20" height="20">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
                Learning Hub
            </a>

            <a href="community.jsp" class="nav-item">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="20" height="20">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
                Community
            </a>

            <a href="settings.jsp" class="nav-item">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="20" height="20">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                Settings
            </a>
        </nav>

        <div class="user-profile">
            <div class="avatar">${sessionScope.user.charAt(0)}</div>
            <div class="user-info">
                <div class="user-name">${sessionScope.user}</div>
                <div class="user-role">Free Plan</div>
            </div>
        </div>
    </aside>

    <main class="main-content">
        <div class="welcome-section">
            <h1 class="welcome-title">Welcome back, ${sessionScope.user}</h1>
            <p class="welcome-subtitle">Here's your daily mental wellness overview.</p>
        </div>

        <div class="quick-actions">
            <a href="chatbot" class="action-card">
                <div class="icon-box bg-blue">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="24" height="24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                    </svg>
                </div>
                <h3 class="card-title">Talk to AI Guide</h3>
                <p class="card-desc">Get instant support and coping strategies anytime.</p>
            </a>

            <a href="learning" class="action-card">
                <div class="icon-box bg-purple">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="24" height="24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                    </svg>
                </div>
                <h3 class="card-title">Learning Hub</h3>
                <p class="card-desc">Continue your courses and access resources.</p>
            </a>

            <a href="#" class="action-card">
                <div class="icon-box bg-green">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="24" height="24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                </div>
                <h3 class="card-title">Daily Check-in</h3>
                <p class="card-desc">Track your mood and clear your mind.</p>
            </a>

            <a href="#" class="action-card">
                <div class="icon-box bg-orange">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="24" height="24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                </div>
                <h3 class="card-title">Book Therapy</h3>
                <p class="card-desc">Schedule a session with a professional.</p>
            </a>
        </div>

        <div class="section-header">
            <h2 class="section-title">Recommended for You</h2>
        </div>

        <div class="rec-card" onclick="window.location.href='learning?action=start&courseId=c1'">
            <div class="rec-icon">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="24" height="24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                    <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
            </div>
            <div class="rec-details" style="flex: 1;">
                <h4>Understanding Anxiety Basics</h4>
                <div class="rec-meta">
                    <span>Module 1</span>
                    <span>•</span>
                    <span>15 min remaining</span>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: 45%;"></div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>