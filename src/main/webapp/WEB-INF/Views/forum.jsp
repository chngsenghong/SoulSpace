<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - Community Forum</title>
    <style>
        /* --- GLOBAL RESET & LAYOUT --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f9fafb;
            color: #1a1a1a;
            height: 100vh;
            overflow: hidden; /* Prevent body scroll, use inner scroll */
        }

        /* --- MAIN CONTENT WRAPPER --- */
        .main-content {
            margin-left: 16rem; /* Matches Sidebar Width */
            height: 100vh;
            overflow-y: auto; /* Allow scrolling */
            transition: margin-left 0.3s ease;
        }

        @media (max-width: 1024px) {
            .main-content { margin-left: 0; }
        }

        .container { padding: 1rem; max-width: 80rem; margin: 0 auto; }
        @media (min-width: 1024px) { .container { padding: 2rem; } }

        /* --- HEADER & BUTTONS --- */
        .page-header { padding-top: 1rem; margin-bottom: 1.5rem; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem; }
        .page-header-left h1 { font-size: 1.875rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.25rem; }
        .page-header-left p { color: #6b7280; }

        .btn-primary {
            padding: 0.75rem 1.5rem;
            background: linear-gradient(135deg, #3b82f6, #9333ea);
            color: white;
            border: none;
            border-radius: 0.5rem;
            font-weight: 500;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            transition: all 0.2s;
        }
        .btn-primary:hover { transform: scale(1.02); box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3); }
        .btn-primary svg { width: 1.25rem; height: 1.25rem; }

        /* --- GUIDELINES BANNER --- */
        .guidelines-banner { background: #eff6ff; border: 1px solid #bfdbfe; border-radius: 0.75rem; padding: 1rem; margin-bottom: 1.5rem; display: flex; align-items: center; gap: 1rem; }
        .guidelines-icon { width: 2.5rem; height: 2.5rem; background: white; border-radius: 0.5rem; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
        .guidelines-icon svg { width: 1.5rem; height: 1.5rem; color: #2563eb; }
        .guidelines-content h4 { font-size: 0.875rem; font-weight: 500; color: #1e40af; }
        .guidelines-content p { font-size: 0.875rem; color: #1e40af; }
        .guidelines-link { color: #2563eb; text-decoration: underline; cursor: pointer; }

        /* --- SEARCH & FILTER --- */
        .search-section { display: flex; gap: 1rem; margin-bottom: 1.5rem; flex-wrap: wrap; }
        .search-input { flex: 1; min-width: 250px; padding: 0.75rem 1rem; border: 1px solid #d1d5db; border-radius: 0.5rem; font-size: 1rem; }
        .search-input:focus { outline: none; border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
        .filter-select { padding: 0.75rem 1rem; border: 1px solid #d1d5db; border-radius: 0.5rem; background: white; cursor: pointer; }

        /* --- CATEGORY TABS --- */
        .category-tabs { display: flex; gap: 0.5rem; overflow-x: auto; margin-bottom: 1.5rem; padding-bottom: 0.5rem; }
        .category-tab { padding: 0.5rem 1rem; background: white; border: 1px solid #d1d5db; border-radius: 9999px; font-size: 0.875rem; cursor: pointer; white-space: nowrap; transition: all 0.2s; }
        .category-tab:hover, .category-tab.active { background: #eff6ff; border-color: #3b82f6; color: #2563eb; }

        /* --- FORUM POSTS --- */
        .forum-list { display: flex; flex-direction: column; gap: 0.75rem; padding-bottom: 2rem; }
        .forum-post { background: white; border-radius: 0.75rem; padding: 1.5rem; box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1); border: 1px solid #e5e7eb; cursor: pointer; transition: all 0.2s; }
        .forum-post:hover { box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); transform: translateY(-2px); }

        /* Pinned Post Styles */
        .forum-post.pinned { background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%); border: 1px solid #fbbf24; }
        .forum-post.pinned .post-author-name::before { content: '📌 '; }

        .post-header { display: flex; align-items: flex-start; gap: 1rem; margin-bottom: 1rem; }
        .post-avatar { width: 3rem; height: 3rem; border-radius: 50%; color: white; display: flex; align-items: center; justify-content: center; font-weight: 500; flex-shrink: 0; }
        .post-header-content { flex: 1; }
        .post-author { display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.25rem; }
        .post-author-name { font-weight: 500; color: #1a1a1a; }
        .post-badge { padding: 0.125rem 0.5rem; background: #eff6ff; color: #1e40af; border-radius: 0.25rem; font-size: 0.75rem; }
        .post-meta { display: flex; align-items: center; gap: 0.75rem; font-size: 0.875rem; color: #6b7280; }
        .post-meta-item { display: flex; align-items: center; gap: 0.25rem; }
        .post-meta-item svg { width: 1rem; height: 1rem; }

        .post-content h3 { font-size: 1.125rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.5rem; }
        .post-excerpt { color: #6b7280; margin-bottom: 1rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
        
        .post-tags { display: flex; flex-wrap: wrap; gap: 0.5rem; margin-bottom: 1rem; }
        .post-tag { padding: 0.25rem 0.75rem; background: #f3f4f6; color: #374151; border-radius: 9999px; font-size: 0.75rem; }

        .post-footer { display: flex; align-items: center; justify-content: space-between; padding-top: 1rem; border-top: 1px solid #e5e7eb; }
        .post-stats { display: flex; gap: 1.5rem; }
        .post-stat { display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; color: #6b7280; }
        .post-stat svg { width: 1.125rem; height: 1.125rem; }

        .post-actions { display: flex; gap: 0.5rem; }
        .post-action-btn { padding: 0.5rem; background: transparent; border: none; cursor: pointer; color: #6b7280; transition: all 0.2s; border-radius: 0.375rem; }
        .post-action-btn:hover { background: #f3f4f6; color: #1a1a1a; }
        .post-action-btn svg { width: 1.25rem; height: 1.25rem; }

        /* --- MODAL --- */
        .modal { display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); align-items: center; justify-content: center; z-index: 1000; padding: 1rem; }
        .modal.active { display: flex; }
        .modal-content { background: white; border-radius: 0.75rem; max-width: 42rem; width: 100%; max-height: 90vh; overflow-y: auto; padding: 2rem; }
        .modal-header { margin-bottom: 1.5rem; }
        .modal-header h2 { font-size: 1.5rem; font-weight: 500; color: #1a1a1a; margin-bottom: 0.5rem; }
        .form-group { margin-bottom: 1.5rem; }
        .form-label { display: block; font-weight: 500; margin-bottom: 0.5rem; }
        .form-input, .form-textarea { width: 100%; padding: 0.75rem 1rem; border: 1px solid #d1d5db; border-radius: 0.5rem; font-size: 1rem; font-family: inherit; }
        .form-textarea { min-height: 150px; resize: vertical; }
        .modal-actions { display: flex; gap: 1rem; justify-content: flex-end; }
        .btn-outline { padding: 0.75rem 1.5rem; background: white; border: 1px solid #d1d5db; border-radius: 0.5rem; font-weight: 500; cursor: pointer; }
        .btn-outline:hover { background: #f9fafb; }

        /* Mobile Toggle (from your other files) */
        .mobile-menu-btn { display: none; margin-right: 1rem; background: none; border: none; cursor: pointer; }
        @media (max-width: 1024px) { .mobile-menu-btn { display: block; } }
    </style>
</head>
<body>

    <jsp:include page="navigation.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="display: flex; align-items: center; margin-bottom: 1rem; @media(min-width: 1024px){display:none;}">
                <button class="mobile-menu-btn" onclick="openSidebar()">
                     <svg style="width:24px;height:24px" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
                    </svg>
                </button>
                <h2 style="font-size: 1.25rem;">Community Forum</h2>
            </div>

            <div class="page-header">
                <div class="page-header-left">
                    <h1>Community Forum</h1>
                    <p>Connect, share, and support each other</p>
                </div>
                <button class="btn-primary" onclick="openNewPostModal()">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4" />
                    </svg>
                    New Post
                </button>
            </div>

            <div class="guidelines-banner">
                <div class="guidelines-icon">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                </div>
                <div class="guidelines-content">
                    <h4>Community Guidelines</h4>
                    <p>Be respectful, supportive, and kind. <span class="guidelines-link" onclick="showGuidelines()">Read full guidelines</span></p>
                </div>
            </div>

            <div class="search-section">
                <input type="text" class="search-input" placeholder="Search discussions...">
                <select class="filter-select">
                    <option>Latest Posts</option>
                    <option>Most Replies</option>
                    <option>Most Helpful</option>
                    <option>Trending</option>
                </select>
            </div>

            <div class="category-tabs">
                <button class="category-tab active" onclick="filterByCategory('all')">All Topics</button>
                <button class="category-tab" onclick="filterByCategory('anxiety')">Anxiety & Stress</button>
                <button class="category-tab" onclick="filterByCategory('depression')">Depression</button>
                <button class="category-tab" onclick="filterByCategory('relationships')">Relationships</button>
                <button class="category-tab" onclick="filterByCategory('selfcare')">Self-Care</button>
                <button class="category-tab" onclick="filterByCategory('success')">Success Stories</button>
            </div>

            <div class="forum-list">
                
                <c:forEach var="post" items="${forumPosts}">
                    <div class="forum-post ${post.pinned ? 'pinned' : ''}" onclick="viewPost(${post.id})">
                        <div class="post-header">
                            <div class="post-avatar" style="background: ${post.authorColor};">
                                ${post.authorInitials}
                            </div>
                            <div class="post-header-content">
                                <div class="post-author">
                                    <span class="post-author-name">${post.authorName}</span>
                                    <c:if test="${post.pinned}">
                                        <span class="post-badge">Moderator</span>
                                    </c:if>
                                </div>
                                <div class="post-meta">
                                    <span class="post-meta-item">
                                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                                        ${post.timeAgo}
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="post-content">
                            <h3>${post.title}</h3>
                            <p class="post-excerpt">${post.excerpt}</p>
                            <div class="post-tags">
                                <c:forEach var="tag" items="${post.tags}">
                                    <span class="post-tag">${tag}</span>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="post-footer">
                            <div class="post-stats">
                                <span class="post-stat">
                                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" /></svg>
                                    ${post.replies} replies
                                </span>
                                <span class="post-stat">
                                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" /></svg>
                                    ${post.views} views
                                </span>
                            </div>
                            <div class="post-actions">
                                <button class="post-action-btn" onclick="likePost(event, ${post.id})" title="Like">
                                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" /></svg>
                                </button>
                                <button class="post-action-btn" onclick="bookmarkPost(event, ${post.id})" title="Bookmark">
                                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" /></svg>
                                </button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>

    <div class="modal" id="newPostModal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Create New Post</h2>
                <p>Share your thoughts, ask questions, or start a discussion</p>
            </div>
            <form id="newPostForm" onsubmit="submitPost(event)">
                <div class="form-group">
                    <label class="form-label" for="postTitle">Title</label>
                    <input type="text" id="postTitle" class="form-input" placeholder="Give your post a clear title" required>
                </div>
                <div class="form-group">
                    <label class="form-label" for="postCategory">Category</label>
                    <select id="postCategory" class="filter-select" style="width: 100%;" required>
                        <option value="">Select a category</option>
                        <option value="anxiety">Anxiety & Stress</option>
                        <option value="depression">Depression</option>
                        <option value="relationships">Relationships</option>
                        <option value="selfcare">Self-Care</option>
                        <option value="success">Success Stories</option>
                        <option value="general">General Discussion</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label" for="postContent">Content</label>
                    <textarea id="postContent" class="form-textarea" placeholder="Share your thoughts..." required></textarea>
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn-outline" onclick="closeNewPostModal()">Cancel</button>
                    <button type="submit" class="btn-primary">Post</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openNewPostModal() { document.getElementById('newPostModal').classList.add('active'); }
        function closeNewPostModal() { document.getElementById('newPostModal').classList.remove('active'); document.getElementById('newPostForm').reset(); }
        
        function submitPost(event) {
            event.preventDefault();
            // Here you would typically use fetch() to send data to a Servlet
            alert('This would send data to the backend!');
            closeNewPostModal();
        }

        function viewPost(id) {
            alert('Navigating to post ID: ' + id);
            // window.location.href = '${pageContext.request.contextPath}/forum/post?id=' + id;
        }

        function filterByCategory(category) {
            document.querySelectorAll('.category-tab').forEach(tab => tab.classList.remove('active'));
            event.target.classList.add('active');
            console.log('Filter by: ' + category);
        }

        function likePost(event, id) {
            event.stopPropagation();
            event.currentTarget.classList.toggle('active');
            // AJAX call to like servlet
        }

        function bookmarkPost(event, id) {
            event.stopPropagation();
            event.currentTarget.classList.toggle('active');
        }

        function showGuidelines() {
            alert('Community Guidelines:\n\n1. Be respectful and kind\n2. No hate speech\n3. Protect privacy');
        }

        document.getElementById('newPostModal').addEventListener('click', function(e) {
            if (e.target === this) closeNewPostModal();
        });
    </script>
</body>
</html>