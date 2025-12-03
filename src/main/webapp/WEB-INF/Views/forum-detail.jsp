<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - ${post.title}</title>
    <style>
        /* Reusing global styles for consistency */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f9fafb; color: #1a1a1a; height: 100vh; overflow: hidden; }
        .main-content { margin-left: 16rem; height: 100vh; overflow-y: auto; transition: margin-left 0.3s ease; }
        @media (max-width: 1024px) { .main-content { margin-left: 0; } }
        .container { padding: 1rem; max-width: 60rem; margin: 0 auto; } /* Narrower for reading */
        @media (min-width: 1024px) { .container { padding: 2rem; } }

        /* Back Button */
        .back-link { display: inline-flex; align-items: center; gap: 0.5rem; text-decoration: none; color: #6b7280; font-weight: 500; margin-bottom: 1.5rem; transition: color 0.2s; }
        .back-link:hover { color: #2563eb; }
        .back-link svg { width: 1.25rem; height: 1.25rem; }

        /* Detail Post Card */
        .post-detail-card { background: white; border-radius: 0.75rem; border: 1px solid #e5e7eb; padding: 2rem; margin-bottom: 2rem; }
        .post-header { display: flex; align-items: center; gap: 1rem; margin-bottom: 1.5rem; border-bottom: 1px solid #f3f4f6; padding-bottom: 1.5rem; }
        .avatar { width: 3rem; height: 3rem; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-weight: 500; font-size: 1.125rem; }
        .meta h1 { font-size: 1.5rem; font-weight: 600; margin-bottom: 0.25rem; }
        .meta-info { color: #6b7280; font-size: 0.875rem; }

        .post-body { font-size: 1.125rem; line-height: 1.7; color: #374151; margin-bottom: 2rem; }
        .tags { display: flex; gap: 0.5rem; margin-bottom: 1rem; }
        .tag { background: #f3f4f6; color: #4b5563; padding: 0.25rem 0.75rem; border-radius: 9999px; font-size: 0.875rem; }

        /* Comments Section */
        .comments-section { max-width: 60rem; margin: 0 auto; }
        .comments-header { font-size: 1.25rem; font-weight: 600; margin-bottom: 1rem; border-bottom: 2px solid #e5e7eb; padding-bottom: 0.5rem; }
        
        .comment-item { background: white; padding: 1.5rem; border-radius: 0.75rem; border: 1px solid #e5e7eb; margin-bottom: 1rem; }
        .comment-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 0.75rem; }
        .comment-user { display: flex; align-items: center; gap: 0.75rem; font-weight: 500; }
        .comment-avatar { width: 2rem; height: 2rem; border-radius: 50%; color: white; display: flex; align-items: center; justify-content: center; font-size: 0.875rem; }
        .comment-time { font-size: 0.75rem; color: #9ca3af; }
        .comment-text { color: #4b5563; line-height: 1.5; }

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

            <a href="${pageContext.request.contextPath}/forum" class="back-link">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M10 19l-7-7m0 0l7-7m-7 7h18" /></svg>
                Back to Forum
            </a>

            <div class="post-detail-card">
                <div class="post-header">
                    <div class="avatar" style="background: ${post.authorColor};">${post.authorInitials}</div>
                    <div class="meta">
                        <h1>${post.title}</h1>
                        <div class="meta-info">
                            Posted by <span style="color:#1a1a1a; font-weight:500;">${post.authorName}</span> • ${post.timeAgo}
                        </div>
                    </div>
                </div>

                <div class="post-body">
                    <p>${post.excerpt}</p>
                    <p style="margin-top: 1rem;">(This is where the full content would go. Since we are using mock data, we are displaying the excerpt here as the main body.)</p>
                </div>

                <div class="tags">
                    <c:forEach var="tag" items="${post.tags}">
                        <span class="tag">#${tag}</span>
                    </c:forEach>
                </div>
            </div>

            <div class="comments-section">
                <h3 class="comments-header">Comments (${post.comments.size()})</h3>
                
                <c:choose>
                    <c:when test="${not empty post.comments}">
                        <c:forEach var="comment" items="${post.comments}">
                            <div class="comment-item">
                                <div class="comment-header">
                                    <div class="comment-user">
                                        <div class="comment-avatar" style="background: ${comment.authorColor};">${comment.authorInitials}</div>
                                        <span>${comment.authorName}</span>
                                    </div>
                                    <span class="comment-time">${comment.timeAgo}</span>
                                </div>
                                <div class="comment-text">
                                    ${comment.content}
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="color: #6b7280; font-style: italic;">No comments yet. Be the first to share your thoughts!</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>

    <script>
        // Use existing sidebar logic if needed or ensure navigation.jsp handles it
    </script>
</body>
</html>