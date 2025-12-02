<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - Learning Hub</title>
    <style>
        /* --- GLOBAL & LAYOUT (Matches Chatbot) --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        
        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif; 
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
            position: fixed;
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

        /* --- LEARNING CONTENT --- */
        .main-content {
            margin-left: 16rem; /* Push content to right */
            width: calc(100% - 16rem);
            min-height: 100vh;
            background: #f9fafb;
        }

        @media (max-width: 1024px) {
            .sidebar { display: none; }
            .main-content { margin-left: 0; width: 100%; }
        }

        /* --- LEARNING SPECIFIC COMPONENTS --- */
        .container { padding: 1rem; max-width: 80rem; margin: 0 auto; }
        @media (min-width: 1024px) { .container { padding: 2rem; } }
        
        .page-header { padding-top: 1rem; margin-bottom: 1.5rem; }
        .page-header h1 { font-size: 1.875rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.25rem; }
        .page-header p { color: #6b7280; }
        
        .tabs { margin-bottom: 1.5rem; }
        .tab-list { display: flex; gap: 0.5rem; border-bottom: 2px solid #e5e7eb; overflow-x: auto; }
        .tab-btn { padding: 0.75rem 1.5rem; border: none; background: transparent; font-weight: 500; cursor: pointer; transition: all 0.2s; border-bottom: 2px solid transparent; margin-bottom: -2px; white-space: nowrap; color: #6b7280; }
        .tab-btn:hover { color: #1a1a1a; }
        .tab-btn.active { color: #2563eb; border-bottom-color: #2563eb; }
        .tab-content { display: none; }
        .tab-content.active { display: block; }

        .filter-section { display: flex; gap: 1rem; margin-bottom: 1.5rem; flex-wrap: wrap; }
        .filter-btn { padding: 0.5rem 1rem; background: white; border: 1px solid #d1d5db; border-radius: 0.5rem; font-size: 0.875rem; cursor: pointer; transition: all 0.2s; }
        .filter-btn:hover, .filter-btn.active { background: #eff6ff; border-color: #3b82f6; color: #2563eb; }

        .stats-grid { display: grid; gap: 1rem; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); margin-bottom: 2rem; }
        .stat-card { background: white; border-radius: 0.75rem; padding: 1.5rem; box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1); border: 1px solid #e5e7eb; }
        .stat-icon { width: 3rem; height: 3rem; background: #eff6ff; border-radius: 0.5rem; display: flex; align-items: center; justify-content: center; margin-bottom: 1rem; }
        .stat-icon svg { width: 1.5rem; height: 1.5rem; color: #2563eb; }
        .stat-value { font-size: 1.875rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.25rem; }
        .stat-label { font-size: 0.875rem; color: #6b7280; }

        .featured-banner { background: linear-gradient(135deg, #2563eb 0%, #9333ea 100%); border-radius: 0.75rem; padding: 2rem; color: white; margin-bottom: 1.5rem; position: relative; overflow: hidden; }
        .featured-banner::before { content: ''; position: absolute; top: -50%; right: -10%; width: 20rem; height: 20rem; background: rgba(255, 255, 255, 0.1); border-radius: 50%; }
        .featured-content { position: relative; z-index: 1; }
        .featured-label { display: inline-flex; align-items: center; gap: 0.5rem; background: rgba(255, 255, 255, 0.2); padding: 0.375rem 0.75rem; border-radius: 9999px; font-size: 0.875rem; margin-bottom: 1rem; }
        .featured-label svg { width: 1rem; height: 1rem; }
        .featured-title { font-size: 1.5rem; font-weight: 500; margin-bottom: 0.5rem; }
        .featured-description { opacity: 0.9; margin-bottom: 1.5rem; max-width: 40rem; }
        .featured-btn { background: white; color: #2563eb; padding: 0.75rem 1.5rem; border-radius: 0.5rem; font-weight: 500; border: none; cursor: pointer; transition: all 0.2s; }
        .featured-btn:hover { transform: scale(1.05); box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }

        .courses-grid { display: grid; gap: 1rem; }
        @media (min-width: 768px) { .courses-grid { grid-template-columns: repeat(2, 1fr); } }
        @media (min-width: 1280px) { .courses-grid { grid-template-columns: repeat(3, 1fr); } }

        .course-card { background: white; border-radius: 0.75rem; overflow: hidden; box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1); border: 1px solid #e5e7eb; transition: all 0.2s; cursor: pointer; }
        .course-card:hover { box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); transform: translateY(-2px); }
        .course-thumbnail { height: 10rem; background: linear-gradient(135deg, #eff6ff 0%, #f3e8ff 100%); display: flex; align-items: center; justify-content: center; position: relative; }
        .course-thumbnail svg { width: 3rem; height: 3rem; color: #2563eb; }
        .course-duration { position: absolute; bottom: 0.75rem; right: 0.75rem; background: rgba(0, 0, 0, 0.75); color: white; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-size: 0.75rem; display: flex; align-items: center; gap: 0.25rem; }
        .course-duration svg { width: 0.875rem; height: 0.875rem; }
        .course-body { padding: 1rem; }
        .course-category { display: inline-flex; align-items: center; padding: 0.25rem 0.5rem; background: #eff6ff; color: #1e40af; border-radius: 0.25rem; font-size: 0.75rem; font-weight: 500; margin-bottom: 0.75rem; }
        .course-title { font-size: 1.125rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.5rem; }
        .course-description { font-size: 0.875rem; color: #6b7280; margin-bottom: 0.75rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
        .course-meta { display: flex; align-items: center; justify-content: space-between; padding-top: 0.75rem; border-top: 1px solid #e5e7eb; }
        .course-lessons { display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: #6b7280; }
        .course-lessons svg { width: 1rem; height: 1rem; }

        .progress-container { display: flex; align-items: center; gap: 0.5rem; }
        .progress-ring { width: 2rem; height: 2rem; position: relative; }
        .progress-ring-circle { transform: rotate(-90deg); }
        .progress-ring-bg { fill: none; stroke: #e5e7eb; stroke-width: 3; }
        .progress-ring-fill { fill: none; stroke: #2563eb; stroke-width: 3; stroke-dasharray: 75.4; stroke-linecap: round; transition: stroke-dashoffset 0.3s; }
        .progress-text { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 0.625rem; font-weight: 500; color: #2563eb; }

        .certificate-card { background: linear-gradient(135deg, #fef3c7 0%, #fed7aa 100%); border-radius: 0.75rem; padding: 1.5rem; border: 1px solid #fcd34d; margin-bottom: 1rem; }
        .certificate-header { display: flex; align-items: center; gap: 1rem; margin-bottom: 1rem; }
        .certificate-icon { width: 3rem; height: 3rem; background: white; border-radius: 0.5rem; display: flex; align-items: center; justify-content: center; }
        .certificate-icon svg { width: 1.5rem; height: 1.5rem; color: #f59e0b; }
        .certificate-info h3 { font-size: 1.125rem; font-weight: 500; color: #92400e; margin-bottom: 0.25rem; }
        .certificate-info p { font-size: 0.875rem; color: #78350f; }
        .certificate-btn { background: white; color: #92400e; padding: 0.5rem 1rem; border-radius: 0.375rem; border: 1px solid #fcd34d; font-weight: 500; cursor: pointer; font-size: 0.875rem; }
        .certificate-btn:hover { background: #fffbeb; }
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
            <a href="dashboard" class="nav-item">
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
            
            <a href="learning" class="nav-item active">
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
            <div class="avatar">U</div>
            <div class="user-info">
                <div class="user-name">Guest User</div>
                <div class="user-role">Free Plan</div>
            </div>
        </div>
    </aside>

    <div class="main-content">
        <div class="container">
            <div class="page-header">
                <h1>Learning Hub</h1>
                <p>Evidence-based mental health education and resources</p>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" /></svg>
                    </div>
                    <div class="stat-value">${stats.coursesInProgress}</div>
                    <div class="stat-label">Courses In Progress</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" /></svg>
                    </div>
                    <div class="stat-value">${stats.certificatesEarned}</div>
                    <div class="stat-label">Certificates Earned</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                    </div>
                    <div class="stat-value">${stats.learningTime}</div>
                    <div class="stat-label">Learning Time</div>
                </div>
            </div>

            <div class="featured-banner">
                <div class="featured-content">
                    <div class="featured-label">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" /></svg>
                        Featured Course
                    </div>
                    <h2 class="featured-title">${featuredCourse.title}</h2>
                    <p class="featured-description">${featuredCourse.description}</p>
                    <button class="featured-btn" onclick="startCourse('${featuredCourse.id}')">Start Learning</button>
                </div>
            </div>

            <div class="tabs">
                <div class="tab-list">
                    <button class="tab-btn active" onclick="switchTab('all')">All Courses</button>
                    <button class="tab-btn" onclick="switchTab('inprogress')">In Progress</button>
                    <button class="tab-btn" onclick="switchTab('certificates')">Certificates</button>
                </div>
            </div>

            <div id="all-tab" class="tab-content active">
                <div class="filter-section">
                    <button class="filter-btn active" onclick="filterCourses('all')">All Topics</button>
                    <button class="filter-btn" onclick="filterCourses('anxiety')">Anxiety</button>
                    <button class="filter-btn" onclick="filterCourses('depression')">Depression</button>
                    <button class="filter-btn" onclick="filterCourses('stress')">Stress Management</button>
                    <button class="filter-btn" onclick="filterCourses('relationships')">Relationships</button>
                </div>

                <div class="courses-grid">
                    <c:forEach var="course" items="${courses}">
                        <div class="course-card" onclick="viewCourse('${course.id}')">
                            <div class="course-thumbnail" style="${course.thumbnailStyle}">
                                ${course.iconSvg}
                                <div class="course-duration">
                                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                                    ${course.duration}
                                </div>
                            </div>
                            <div class="course-body">
                                <span class="course-category">${course.category}</span>
                                <h3 class="course-title">${course.title}</h3>
                                <p class="course-description">${course.description}</p>
                                <div class="course-meta">
                                    <div class="course-lessons">
                                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z" /></svg>
                                        ${course.lessonsCount} lessons
                                    </div>
                                    <div class="progress-container">
                                        <c:choose>
                                            <c:when test="${course.progress > 0}">
                                                <c:set var="offset" value="${75.4 - (75.4 * course.progress / 100)}" />
                                                <svg class="progress-ring" width="32" height="32">
                                                    <circle class="progress-ring-bg" cx="16" cy="16" r="12"/>
                                                    <circle class="progress-ring-fill" cx="16" cy="16" r="12" style="stroke-dashoffset: ${offset};"/>
                                                </svg>
                                                <span class="progress-text" style="position: static; transform: none;">${course.progress}%</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="font-size: 0.875rem; color: #6b7280;">New</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div id="inprogress-tab" class="tab-content">
                <div class="courses-grid">
                    <c:forEach var="course" items="${inProgressCourses}">
                        <div class="course-card" onclick="viewCourse('${course.id}')">
                            <div class="course-thumbnail" style="${course.thumbnailStyle}">
                                ${course.iconSvg}
                                <div class="course-duration">
                                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                                    ${course.duration}
                                </div>
                            </div>
                            <div class="course-body">
                                <span class="course-category">${course.category}</span>
                                <h3 class="course-title">${course.title}</h3>
                                <div class="course-meta">
                                    <div class="course-lessons">
                                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z" /></svg>
                                        ${course.completedLessons}/${course.lessonsCount} lessons
                                    </div>
                                    <div class="progress-container">
                                        <c:set var="offset" value="${75.4 - (75.4 * course.progress / 100)}" />
                                        <svg class="progress-ring" width="32" height="32">
                                            <circle class="progress-ring-bg" cx="16" cy="16" r="12"/>
                                            <circle class="progress-ring-fill" cx="16" cy="16" r="12" style="stroke-dashoffset: ${offset};"/>
                                        </svg>
                                        <span class="progress-text" style="position: static; transform: none;">${course.progress}%</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div id="certificates-tab" class="tab-content">
                <c:forEach var="cert" items="${certificates}">
                    <div class="certificate-card">
                        <div class="certificate-header">
                            <div class="certificate-icon">
                                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" /></svg>
                            </div>
                            <div class="certificate-info">
                                <h3>${cert.courseName}</h3>
                                <p>Completed on ${cert.completionDate}</p>
                            </div>
                        </div>
                        <button class="certificate-btn" onclick="downloadCertificate('${cert.id}')">Download Certificate</button>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <script>
        function switchTab(tab) {
            document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
            event.target.classList.add('active');
            document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
            document.getElementById(tab + '-tab').classList.add('active');
        }

        function filterCourses(category) {
            document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
            event.target.classList.add('active');
            console.log('Filtering by:', category);
        }

        function viewCourse(id) {
            window.location.href = 'learning?action=start&courseId=' + id;
        }

        function startCourse(courseId) {
            window.location.href = 'learning?action=start&courseId=' + courseId;
        }

        function downloadCertificate(id) {
            window.location.href = 'learning?action=download&certId=' + id;
        }
    </script>
</body>
</html>