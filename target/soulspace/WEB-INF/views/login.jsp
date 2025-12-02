<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - Login</title>
    <style>
        /* Keeping your exact styles */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif; font-size: 16px; line-height: 1.5; color: #1a1a1a; }
        .container { min-height: 100vh; display: grid; background: #f9fafb; }
        @media (min-width: 1024px) { .container { grid-template-columns: 1fr 1fr; } }
        
        /* Branding Side */
        .branding-side { display: none; background: linear-gradient(135deg, #2563eb 0%, #3b82f6 50%, #9333ea 100%); color: white; padding: 3rem; flex-direction: column; justify-content: space-between; position: relative; overflow: hidden; }
        @media (min-width: 1024px) { .branding-side { display: flex; } }
        .branding-content { position: relative; z-index: 1; }
        .logo { display: flex; align-items: center; gap: 0.75rem; font-size: 1.5rem; font-weight: 700; margin-bottom: 2rem; }
        .logo-icon { width: 2.5rem; height: 2.5rem; background: rgba(255, 255, 255, 0.2); border-radius: 0.5rem; display: flex; align-items: center; justify-content: center; backdrop-filter: blur(8px); }
        .quote-container { margin-top: auto; }
        .quote { font-size: 1.5rem; font-weight: 500; line-height: 1.4; margin-bottom: 1rem; }
        .quote-author { opacity: 0.8; }
        
        /* Login Side */
        .login-side { display: flex; align-items: center; justify-content: center; padding: 2rem; }
        .login-card { width: 100%; max-width: 28rem; }
        .login-header { margin-bottom: 2rem; }
        .login-header h1 { font-size: 1.875rem; font-weight: 700; color: #111827; margin-bottom: 0.5rem; }
        .login-header p { color: #6b7280; }
        .login-header a { color: #2563eb; text-decoration: none; font-weight: 500; }
        
        /* Form */
        .form-group { margin-bottom: 1.25rem; }
        .form-label { display: block; font-size: 0.875rem; font-weight: 500; color: #374151; margin-bottom: 0.5rem; }
        .input-wrapper { position: relative; }
        .form-input { width: 100%; padding: 0.75rem 1rem; padding-left: 2.5rem; border: 1px solid #d1d5db; border-radius: 0.5rem; font-size: 0.875rem; transition: all 0.2s; outline: none; }
        .form-input:focus { border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1); }
        .input-icon { position: absolute; left: 0.75rem; top: 50%; transform: translateY(-50%); color: #9ca3af; pointer-events: none; }
        .input-icon svg { width: 1.25rem; height: 1.25rem; }
        .password-toggle { position: absolute; right: 0.75rem; top: 50%; transform: translateY(-50%); color: #9ca3af; background: none; border: none; cursor: pointer; padding: 0; }
        .password-toggle:hover { color: #6b7280; }
        
        .form-actions { display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.5rem; }
        .remember-me { display: flex; align-items: center; gap: 0.5rem; }
        .remember-me input { width: 1rem; height: 1rem; border-radius: 0.25rem; border-color: #d1d5db; color: #2563eb; }
        .remember-me label { font-size: 0.875rem; color: #4b5563; }
        .forgot-password { font-size: 0.875rem; color: #2563eb; text-decoration: none; font-weight: 500; }
        
        .submit-btn { width: 100%; background: #2563eb; color: white; padding: 0.75rem; border: none; border-radius: 0.5rem; font-weight: 500; font-size: 1rem; cursor: pointer; transition: all 0.2s; display: flex; align-items: center; justify-content: center; gap: 0.5rem; }
        .submit-btn:hover { background: #1d4ed8; transform: translateY(-1px); }
        
        .divider { display: flex; align-items: center; gap: 1rem; margin: 1.5rem 0; color: #9ca3af; font-size: 0.875rem; }
        .divider::before, .divider::after { content: ''; flex: 1; height: 1px; background: #e5e7eb; }
        
        .social-login { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
        .social-btn { display: flex; align-items: center; justify-content: center; gap: 0.5rem; padding: 0.75rem; background: white; border: 1px solid #d1d5db; border-radius: 0.5rem; color: #374151; font-weight: 500; font-size: 0.875rem; cursor: pointer; transition: all 0.2s; }
        .social-btn:hover { background: #f9fafb; border-color: #9ca3af; }

        .error-message {
            background-color: #fee2e2;
            color: #b91c1c;
            padding: 0.75rem;
            border-radius: 0.5rem;
            margin-bottom: 1.5rem;
            font-size: 0.875rem;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="branding-side">
            <div class="branding-content">
                <div class="logo">
                    <div class="logo-icon">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="24" height="24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                        </svg>
                    </div>
                    SoulSpace
                </div>
            </div>
            <div class="quote-container">
                <p class="quote">"Mental health is not a destination, but a process. It's about how you drive, not where you're going."</p>
                <p class="quote-author">Noam Shpancer</p>
            </div>
        </div>

        <div class="login-side">
            <div class="login-card">
                <div class="login-header">
                    <h1>Welcome back</h1>
                    <p>Don't have an account? <a href="register.jsp">Create account</a></p>
                </div>

                <% if(request.getAttribute("errorMessage") != null) { %>
                    <div class="error-message">
                        <%= request.getAttribute("errorMessage") %>
                    </div>
                <% } %>

                <form action="login" method="post">
                    <div class="form-group">
                        <label class="form-label" for="email">Email address</label>
                        <div class="input-wrapper">
                            <input type="email" id="email" name="email" class="form-input" placeholder="name@example.com" required>
                            <span class="input-icon">
                                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207" />
                                </svg>
                            </span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="password">Password</label>
                        <div class="input-wrapper">
                            <input type="password" id="password" name="password" class="form-input" placeholder="Enter your password" required>
                            <span class="input-icon">
                                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                                </svg>
                            </span>
                            <button type="button" class="password-toggle" onclick="togglePassword()">
                                <svg id="eyeIcon" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="20" height="20">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                                </svg>
                            </button>
                        </div>
                    </div>

                    <div class="form-actions">
                        <div class="remember-me">
                            <input type="checkbox" id="remember">
                            <label for="remember">Remember me</label>
                        </div>
                        <a href="#" class="forgot-password">Forgot password?</a>
                    </div>

                    <button type="submit" class="submit-btn">
                        Sign in
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" width="20" height="20">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M14 5l7 7m0 0l-7 7m7-7H3" />
                        </svg>
                    </button>
                </form>

                <div class="divider">Or continue with</div>

                <div class="social-login">
                    <button type="button" class="social-btn">Google</button>
                    <button type="button" class="social-btn">Microsoft</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        function togglePassword() {
            const passwordInput = document.getElementById('password');
            const eyeIcon = document.getElementById('eyeIcon');
            
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                // Slash eye icon
                eyeIcon.innerHTML = '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />';
            } else {
                passwordInput.type = 'password';
                // Normal eye icon
                eyeIcon.innerHTML = '<path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />';
            }
        }
    </script>
</body>
</html>