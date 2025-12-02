<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - Settings</title>
    <style>
        /* --- GLOBAL RESET --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f9fafb;
            color: #1a1a1a;
            height: 100vh;
            overflow: hidden; 
        }

        /* --- LAYOUT WRAPPERS --- */
        .main-content {
            margin-left: 16rem;
            height: 100vh;
            overflow-y: auto;
            transition: margin-left 0.3s ease;
        }
        @media (max-width: 1024px) { .main-content { margin-left: 0; } }
        .container { padding: 1rem; max-width: 64rem; margin: 0 auto; }
        @media (min-width: 1024px) { .container { padding: 2rem; } }

        /* --- HEADER --- */
        .page-header { margin-bottom: 2rem; }
        .page-header h1 { font-size: 1.875rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.5rem; }
        .page-header p { color: #6b7280; }

        /* --- SETTINGS LAYOUT --- */
        .settings-grid { display: grid; gap: 1.5rem; grid-template-columns: 1fr; }
        @media (min-width: 768px) { 
            .settings-grid { grid-template-columns: 250px 1fr; } 
        }

        /* Sidebar Tabs */
        .settings-nav { background: white; border-radius: 0.75rem; border: 1px solid #e5e7eb; overflow: hidden; height: fit-content; }
        .settings-tab { display: flex; align-items: center; gap: 0.75rem; width: 100%; padding: 1rem; border: none; background: none; text-align: left; cursor: pointer; color: #6b7280; font-size: 0.875rem; font-weight: 500; border-bottom: 1px solid #f3f4f6; transition: all 0.2s; }
        .settings-tab:last-child { border-bottom: none; }
        .settings-tab:hover { background: #f9fafb; color: #1a1a1a; }
        .settings-tab.active { background: #eff6ff; color: #2563eb; border-left: 3px solid #2563eb; }
        .settings-tab svg { width: 1.25rem; height: 1.25rem; }

        /* Card Styles */
        .card { background: white; border-radius: 0.75rem; border: 1px solid #e5e7eb; box-shadow: 0 1px 2px rgba(0,0,0,0.05); overflow: hidden; }
        .card-header { padding: 1.5rem; border-bottom: 1px solid #e5e7eb; }
        .card-header h3 { font-size: 1.125rem; font-weight: 500; color: #1a1a1a; }
        .card-header p { font-size: 0.875rem; color: #6b7280; margin-top: 0.25rem; }
        .card-body { padding: 1.5rem; }
        .card-footer { padding: 1rem 1.5rem; background: #f9fafb; border-top: 1px solid #e5e7eb; display: flex; justify-content: flex-end; gap: 0.75rem; }

        /* Forms */
        .form-grid { display: grid; gap: 1.5rem; grid-template-columns: 1fr; }
        @media (min-width: 640px) { .form-grid { grid-template-columns: 1fr 1fr; } }
        
        .form-group { margin-bottom: 1.5rem; }
        .form-label { display: block; font-size: 0.875rem; font-weight: 500; color: #374151; margin-bottom: 0.5rem; }
        .form-input, .form-textarea { width: 100%; padding: 0.625rem 0.875rem; border: 1px solid #d1d5db; border-radius: 0.5rem; font-size: 0.875rem; color: #1a1a1a; transition: border-color 0.2s; font-family: inherit; }
        .form-input:focus, .form-textarea:focus { outline: none; border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
        .form-textarea { min-height: 100px; resize: vertical; }

        /* Profile Avatar Section */
        .avatar-upload { display: flex; align-items: center; gap: 1.5rem; margin-bottom: 2rem; }
        .current-avatar { width: 5rem; height: 5rem; border-radius: 50%; background: linear-gradient(135deg, #3b82f6, #9333ea); color: white; display: flex; align-items: center; justify-content: center; font-size: 1.5rem; font-weight: 600; }
        .upload-btn { padding: 0.5rem 1rem; background: white; border: 1px solid #d1d5db; border-radius: 0.5rem; font-size: 0.875rem; font-weight: 500; color: #374151; cursor: pointer; transition: all 0.2s; }
        .upload-btn:hover { background: #f9fafb; border-color: #9ca3af; }
        .upload-info { font-size: 0.75rem; color: #6b7280; margin-top: 0.25rem; }

        /* Buttons */
        .btn-primary { padding: 0.625rem 1.25rem; background: #2563eb; color: white; border: none; border-radius: 0.5rem; font-size: 0.875rem; font-weight: 500; cursor: pointer; transition: background 0.2s; }
        .btn-primary:hover { background: #1d4ed8; }
        .btn-secondary { padding: 0.625rem 1.25rem; background: white; border: 1px solid #d1d5db; color: #374151; border-radius: 0.5rem; font-size: 0.875rem; font-weight: 500; cursor: pointer; transition: background 0.2s; }
        .btn-secondary:hover { background: #f9fafb; }

        /* Toggles */
        .toggle-group { display: flex; align-items: center; justify-content: space-between; padding: 1rem 0; border-bottom: 1px solid #f3f4f6; }
        .toggle-group:last-child { border-bottom: none; }
        .toggle-info h4 { font-size: 0.875rem; font-weight: 500; color: #1a1a1a; }
        .toggle-info p { font-size: 0.75rem; color: #6b7280; }
        .toggle-input { accent-color: #2563eb; width: 1.25rem; height: 1.25rem; cursor: pointer; }

        /* Mobile Menu */
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
                <h1>Account Settings</h1>
                <p>Manage your profile information and preferences</p>
            </div>

            <div class="settings-grid">
                
                <nav class="settings-nav">
                    <button class="settings-tab active" onclick="showTab('profile')">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                        Profile
                    </button>
                    <button class="settings-tab" onclick="showTab('notifications')">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" /></svg>
                        Notifications
                    </button>
                    <button class="settings-tab" onclick="showTab('security')">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" /></svg>
                        Security
                    </button>
                </nav>

                <div class="settings-content">
                    
                    <form action="settings" method="post">
                        
                        <div class="card" id="tab-profile">
                            <div class="card-header">
                                <h3>Public Profile</h3>
                                <p>This information will be displayed publicly</p>
                            </div>
                            <div class="card-body">
                                <div class="avatar-upload">
                                    <div class="current-avatar">${user.initials}</div>
                                    <div>
                                        <button type="button" class="upload-btn">Change Avatar</button>
                                        <p class="upload-info">JPG, GIF or PNG. Max size of 800K</p>
                                    </div>
                                </div>

                                <div class="form-grid">
                                    <div class="form-group">
                                        <label class="form-label">First Name</label>
                                        <input type="text" class="form-input" name="firstName" value="${user.firstName}">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Last Name</label>
                                        <input type="text" class="form-input" name="lastName" value="${user.lastName}">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="form-label">Email Address</label>
                                    <input type="email" class="form-input" name="email" value="${user.email}">
                                </div>

                                <div class="form-group">
                                    <label class="form-label">Bio</label>
                                    <textarea class="form-textarea" name="bio">${user.bio}</textarea>
                                </div>
                            </div>
                            <div class="card-footer">
                                <button type="button" class="btn-secondary">Cancel</button>
                                <button type="submit" class="btn-primary">Save Changes</button>
                            </div>
                        </div>

                        <div class="card" id="tab-notifications" style="display: none;">
                            <div class="card-header">
                                <h3>Notification Preferences</h3>
                                <p>Manage how we communicate with you</p>
                            </div>
                            <div class="card-body">
                                <div class="toggle-group">
                                    <div class="toggle-info">
                                        <h4>Email Notifications</h4>
                                        <p>Receive weekly wellness summaries</p>
                                    </div>
                                    <input type="checkbox" class="toggle-input" name="emailNotif" ${user.emailNotifications ? 'checked' : ''}>
                                </div>
                                <div class="toggle-group">
                                    <div class="toggle-info">
                                        <h4>Push Notifications</h4>
                                        <p>Receive alerts about new messages</p>
                                    </div>
                                    <input type="checkbox" class="toggle-input" name="pushNotif" ${user.pushNotifications ? 'checked' : ''}>
                                </div>
                            </div>
                            <div class="card-footer">
                                <button type="submit" class="btn-primary">Save Preferences</button>
                            </div>
                        </div>

                    </form>

                </div>
            </div>
        </div>
    </main>

    <script>
        function showTab(tabName) {
            // Hide all tabs
            document.getElementById('tab-profile').style.display = 'none';
            document.getElementById('tab-notifications').style.display = 'none';
            // Show selected
            document.getElementById('tab-' + tabName).style.display = 'block';

            // Update active state in sidebar
            const buttons = document.querySelectorAll('.settings-tab');
            buttons.forEach(btn => btn.classList.remove('active'));
            event.currentTarget.classList.add('active');
        }

        // Check URL params for success message
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('saved') === 'true') {
            alert('Settings saved successfully!');
            // Clean URL
            window.history.replaceState(null, null, window.location.pathname);
        }
    </script>
</body>
</html>