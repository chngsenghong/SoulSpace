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
        .card-actions { display: flex; gap: 0.5rem; margin-left: auto; }
        .action-icon-btn { background: none; border: none; cursor: pointer; color: #9ca3af; padding: 0.25rem; transition: color 0.2s; }
        .action-icon-btn:hover { color: #2563eb; }
        .action-icon-btn.delete:hover { color: #ef4444; }
    </style>
</head>
<body>

    <jsp:include page="navigation.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div class="page-header">
                <div class="page-header-left">
                    <h1>Community Forum</h1>
                    <p>Connect, share, and support each other</p>
                </div>
                <button class="btn-primary" onclick="openModal('create')">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4" /></svg>
                    New Post
                </button>
            </div>

            <div class="forum-list">
                <c:forEach var="post" items="${forumPosts}">
                    <div class="forum-post ${post.pinned ? 'pinned' : ''}">
                        
                        <div class="post-header">
                            <div class="post-avatar" style="background: ${post.authorColor};">${post.authorInitials}</div>
                            <div class="post-header-content">
                                <div class="post-author">
                                    <span class="post-author-name">${post.authorName}</span>
                                    <c:if test="${post.pinned}"><span class="post-badge">Moderator</span></c:if>
                                </div>
                                <div class="post-meta">
                                    <span class="post-meta-item">${post.timeAgo}</span>
                                </div>
                            </div>
                            
                            <div class="card-actions">
                                <button class="action-icon-btn" title="Edit" onclick="openModal('edit', ${post.id}, '${post.title}', '${post.category}', '${post.excerpt}')">
                                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
                                </button>
                                
                                <form action="forum" method="post" style="display:inline;" onsubmit="return confirm('Delete this post?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${post.id}">
                                    <button type="submit" class="action-icon-btn delete" title="Delete">
                                        <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
                                    </button>
                                </form>
                            </div>
                        </div>

                        <div class="post-content" onclick="window.location.href='${pageContext.request.contextPath}/forum/post?id=${post.id}'" style="cursor: pointer;">
                            <h3>${post.title}</h3>
                            <p class="post-excerpt">${post.excerpt}</p>
                            <div class="post-tags">
                                <c:forEach var="tag" items="${post.tags}"><span class="post-tag">${tag}</span></c:forEach>
                            </div>
                        </div>

                        <div class="post-footer">
                             </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>

    <div class="modal" id="postModal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 id="modalTitle">Create New Post</h2>
            </div>
            <form action="forum" method="post">
                <input type="hidden" name="action" id="formAction" value="create">
                <input type="hidden" name="id" id="postId" value="">

                <div class="form-group">
                    <label class="form-label">Title</label>
                    <input type="text" name="title" id="inputTitle" class="form-input" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Category</label>
                    <select name="category" id="inputCategory" class="filter-select" style="width: 100%;" required>
                        <option value="General">General</option>
                        <option value="Anxiety">Anxiety & Stress</option>
                        <option value="Depression">Depression</option>
                        <option value="Self-Care">Self-Care</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Content</label>
                    <textarea name="content" id="inputContent" class="form-textarea" required></textarea>
                </div>
                <div class="form-group" id="tagsGroup">
                    <label class="form-label">Tags (comma separated)</label>
                    <input type="text" name="tags" class="form-input" placeholder="e.g. stress, help">
                </div>
                
                <div class="modal-actions">
                    <button type="button" class="btn-outline" onclick="closeModal()">Cancel</button>
                    <button type="submit" class="btn-primary" id="submitBtn">Post</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openModal(mode, id, title, category, content) {
            const modal = document.getElementById('postModal');
            const formAction = document.getElementById('formAction');
            
            if (mode === 'edit') {
                // Set Modal for Editing
                document.getElementById('modalTitle').innerText = 'Edit Post';
                document.getElementById('submitBtn').innerText = 'Update';
                formAction.value = 'update';
                
                // Fill data
                document.getElementById('postId').value = id;
                document.getElementById('inputTitle').value = title;
                document.getElementById('inputCategory').value = category;
                document.getElementById('inputContent').value = content;
                
                // Hide tags for edit (optional simplification)
                document.getElementById('tagsGroup').style.display = 'none';
            } else {
                // Set Modal for Creating
                document.getElementById('modalTitle').innerText = 'Create New Post';
                document.getElementById('submitBtn').innerText = 'Post';
                formAction.value = 'create';
                
                // Clear data
                document.getElementById('postId').value = '';
                document.getElementById('inputTitle').value = '';
                document.getElementById('inputCategory').value = 'General';
                document.getElementById('inputContent').value = '';
                document.getElementById('tagsGroup').style.display = 'block';
            }
            
            modal.classList.add('active');
        }

        function closeModal() {
            document.getElementById('postModal').classList.remove('active');
        }

        // Close on outside click
        window.onclick = function(e) {
            if (e.target.classList.contains('modal')) closeModal();
        }
    </script>
</body>
</html>