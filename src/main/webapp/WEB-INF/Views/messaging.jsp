<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - Messages</title>
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
            display: flex;
            flex-direction: column;
            transition: margin-left 0.3s ease;
        }

        @media (max-width: 1024px) {
            .main-content { margin-left: 0; }
        }

        .messaging-container {
            display: flex;
            flex: 1; 
            background: white;
            overflow: hidden; 
        }

        /* --- SIDEBAR --- */
        .conversations-sidebar { width: 320px; border-right: 1px solid #e5e7eb; display: flex; flex-direction: column; background: #fff; }
        
        .sidebar-header { padding: 1.5rem 1rem 1rem; border-bottom: 1px solid #e5e7eb; }
        .sidebar-header h2 { font-size: 1.5rem; font-weight: 500; margin-bottom: 1rem; }
        
        .search-box { position: relative; }
        .search-input { width: 100%; padding: 0.75rem 1rem 0.75rem 2.5rem; border: 1px solid #d1d5db; border-radius: 0.5rem; font-size: 0.875rem; }
        .search-icon { position: absolute; left: 0.8rem; top: 50%; transform: translateY(-50%); color: #6b7280; width: 1.1rem; }

        .conversations-list { flex: 1; overflow-y: auto; }
        
        .conversation-item { padding: 1rem; border-bottom: 1px solid #f3f4f6; cursor: pointer; display: flex; gap: 0.75rem; align-items: center; transition: all 0.2s; }
        .conversation-item:hover { background: #f9fafb; }
        .conversation-item.active { background: #eff6ff; border-left: 3px solid #2563eb; }
        
        .conversation-avatar { width: 3rem; height: 3rem; background: #2563eb; color: white; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: 500; flex-shrink: 0; position: relative; font-size: 1rem; }
        .online-indicator { position: absolute; bottom: 0; right: 0; width: 0.75rem; height: 0.75rem; background: #10b981; border: 2px solid white; border-radius: 50%; display: none; }
        .online-indicator.visible { display: block; }
        
        .conversation-details { flex: 1; min-width: 0; }
        .conversation-header { display: flex; justify-content: space-between; margin-bottom: 0.25rem; }
        .conversation-name { font-weight: 500; color: #1f2937; }
        .conversation-time { font-size: 0.75rem; color: #6b7280; }
        .conversation-preview { font-size: 0.85rem; color: #6b7280; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
        .conversation-item.unread .conversation-preview { color: #111827; font-weight: 500; }
        
        .unread-badge { min-width: 1.25rem; height: 1.25rem; background: #2563eb; color: white; border-radius: 99px; display: flex; align-items: center; justify-content: center; font-size: 0.75rem; font-weight: 500; padding: 0 0.4rem; }

        /* --- CHAT AREA --- */
        .chat-area { flex: 1; display: flex; flex-direction: column; background: #f9fafb; }
        
        .chat-header { padding: 1rem 1.5rem; background: white; border-bottom: 1px solid #e5e7eb; display: flex; align-items: center; justify-content: space-between; flex-shrink: 0; }
        .chat-header-left { display: flex; align-items: center; gap: 1rem; }
        
        .chat-messages { flex: 1; overflow-y: auto; padding: 1.5rem; display: flex; flex-direction: column; }
        
        .message-group { margin-bottom: 1.5rem; }
        .message-date { text-align: center; color: #9ca3af; font-size: 0.75rem; margin-bottom: 1rem; font-weight: 500; }
        
        .message { display: flex; gap: 0.75rem; margin-bottom: 0.5rem; max-width: 75%; animation: fadeIn 0.2s ease; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(5px); } to { opacity: 1; transform: translateY(0); } }
        
        .message.sent { align-self: flex-end; flex-direction: row-reverse; }
        .message.received { align-self: flex-start; }
        
        .message-avatar-small { width: 2rem; height: 2rem; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-size: 0.75rem; flex-shrink: 0; }
        
        .message-bubble { padding: 0.75rem 1rem; border-radius: 1rem; background: white; box-shadow: 0 1px 2px rgba(0,0,0,0.05); position: relative; line-height: 1.5; color: #374151; }
        .message.sent .message-bubble { background: #2563eb; color: white; border-bottom-right-radius: 0.25rem; }
        .message.received .message-bubble { border-bottom-left-radius: 0.25rem; }
        
        .message-time { font-size: 0.7rem; margin-top: 0.25rem; color: #9ca3af; padding: 0 0.25rem; }
        .message.sent .message-time { text-align: right; }

        /* Input Area */
        .chat-input-container { padding: 1rem 1.5rem; background: white; border-top: 1px solid #e5e7eb; flex-shrink: 0; }
        .chat-input-wrapper { display: flex; gap: 0.75rem; align-items: flex-end; background: #f9fafb; padding: 0.5rem; border-radius: 1.5rem; border: 1px solid #e5e7eb; }
        .chat-input { flex: 1; padding: 0.5rem 1rem; border: none; background: transparent; font-size: 1rem; resize: none; max-height: 8rem; font-family: inherit; }
        .chat-input:focus { outline: none; }
        
        .action-btn { width: 2.5rem; height: 2.5rem; border: none; background: transparent; color: #6b7280; cursor: pointer; border-radius: 50%; display: flex; align-items: center; justify-content: center; transition: all 0.2s; flex-shrink: 0; }
        .action-btn:hover { background: #e5e7eb; color: #1f2937; }
        
        .send-btn { background: #2563eb; color: white; }
        .send-btn:hover { background: #1d4ed8; color: white; transform: scale(1.05); }

        /* Mobile */
        .mobile-menu-btn { display: none; background: none; border: none; cursor: pointer; margin-right: 0.5rem; }
        .back-btn { display: none; background: none; border: none; cursor: pointer; color: #6b7280; }
        
        @media (max-width: 768px) {
            .mobile-menu-btn { display: block; }
            .conversations-sidebar { width: 100%; }
            .chat-area { display: none; }
            
            /* Mobile View States */
            .conversations-sidebar.hidden { display: none; }
            .chat-area.active { display: flex; position: fixed; top: 0; left: 0; width: 100%; height: 100%; z-index: 50; }
            .back-btn { display: block; }
        }
    </style>
</head>
<body>

    <jsp:include page="navigation.jsp" />

    <main class="main-content">
        <div class="messaging-container">
            
            <div class="conversations-sidebar" id="sidebarList">
                <div class="sidebar-header">
                    <div style="display:flex; align-items:center; justify-content:space-between;">
                        <div style="display:flex; align-items:center;">
                            <button class="mobile-menu-btn" onclick="openSidebar()">
                                <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
                            </button>
                            <h2>Messages</h2>
                        </div>
                        <button class="action-btn" title="New Message">
                            <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>
                        </button>
                    </div>
                    
                    <div class="search-box">
                        <svg class="search-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
                        <input type="text" class="search-input" id="searchInput" placeholder="Search conversations..." onkeyup="filterConversations()">
                    </div>
                </div>

                <div class="conversations-list" id="conversationsContainer">
                    </div>
            </div>

            <div class="chat-area" id="chatWindow" style="display: none;"> <div class="chat-header">
                    <div class="chat-header-left">
                        <button class="back-btn" onclick="closeChatMobile()">
                            <svg width="24" height="24" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path></svg>
                        </button>
                        <div class="conversation-avatar" id="headerAvatar" style="width: 2.5rem; height: 2.5rem;">
                            </div>
                        <div>
                            <h3 id="headerName" style="margin:0; font-size:1rem; font-weight:600;"></h3>
                            <span id="headerStatus" style="font-size:0.75rem; color:#10b981;">Online</span>
                        </div>
                    </div>
                    <div style="display:flex; gap:0.5rem;">
                        <button class="action-btn" title="Call"><svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"></path></svg></button>
                        <button class="action-btn" title="Video"><svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z"></path></svg></button>
                        <button class="action-btn" title="Info"><svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg></button>
                    </div>
                </div>

                <div class="chat-messages" id="messagesContainer">
                    </div>

                <div class="chat-input-container">
                    <div class="chat-input-wrapper">
                        <button class="action-btn" title="Attach"><svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13"></path></svg></button>
                        <textarea id="messageInput" class="chat-input" rows="1" placeholder="Type a message..." onkeydown="handleEnter(event)"></textarea>
                        <button class="action-btn send-btn" onclick="sendMessage()">
                            <svg width="18" height="18" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"></path></svg>
                        </button>
                    </div>
                </div>
            </div>

            <div id="emptyState" style="flex:1; display:flex; flex-direction:column; align-items:center; justify-content:center; background:#f9fafb;">
                <div style="width:4rem; height:4rem; background:#e5e7eb; border-radius:50%; display:flex; align-items:center; justify-content:center; margin-bottom:1rem; color:#6b7280;">
                    <svg width="32" height="32" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
                </div>
                <h3 style="color:#374151; margin-bottom:0.5rem;">Your Messages</h3>
                <p style="color:#6b7280;">Select a conversation to start chatting</p>
            </div>

        </div>
    </main>

    <script>
        const currentUser = "You";
        let activeChatId = null;

        const conversations = [
            {
                id: 1,
                name: "Dr. Sarah Johnson",
                role: "Clinical Psychologist",
                avatar: "SJ",
                color: "#3b82f6",
                online: true,
                unread: 2,
                messages: [
                    { id: 1, sender: "SJ", text: "Good morning! How have you been feeling since our last session?", time: "09:15 AM", type: "received" },
                    { id: 2, sender: "You", text: "Hi Dr. Johnson! I've been feeling much better. The breathing exercises really helped.", time: "09:20 AM", type: "sent" },
                    { id: 3, sender: "SJ", text: "That's wonderful to hear! Consistency is key with those exercises.", time: "09:22 AM", type: "received" },
                    { id: 4, sender: "SJ", text: "I've attached a worksheet for you to track your progress this week.", time: "09:23 AM", type: "received" }
                ]
            },
            {
                id: 2,
                name: "Dr. Michael Chen",
                role: "Therapist",
                avatar: "MC",
                color: "#9333ea",
                online: false,
                unread: 0,
                messages: [
                    { id: 1, sender: "MC", text: "Please remember to fill out the pre-session form before tomorrow.", time: "Yesterday", type: "received" },
                    { id: 2, sender: "You", text: "Will do, thanks for the reminder.", time: "Yesterday", type: "sent" }
                ]
            },
            {
                id: 3,
                name: "Dr. Emily Williams",
                role: "Psychiatrist",
                avatar: "EW",
                color: "#10b981",
                online: true,
                unread: 1,
                messages: [
                    { id: 1, sender: "EW", text: "I have reviewed your latest assessment scores.", time: "2 days ago", type: "received" },
                    { id: 2, sender: "EW", text: "Everything looks stable. Let's keep the current plan.", time: "2 days ago", type: "received" }
                ]
            },
            {
                id: 4,
                name: "SoulSpace Support",
                role: "Admin",
                avatar: "SS",
                color: "#f59e0b",
                online: true,
                unread: 0,
                messages: [
                    { id: 1, sender: "SS", text: "Welcome to SoulSpace! Let us know if you have any technical issues.", time: "Nov 01", type: "received" }
                ]
            }
        ];

        // --- 2. RENDER FUNCTIONS ---

        function renderSidebar() {
            const container = document.getElementById('conversationsContainer');
            container.innerHTML = '';

            conversations.forEach(convo => {
                const lastMsg = convo.messages[convo.messages.length - 1];
                const isActive = convo.id === activeChatId ? 'active' : '';
                const isUnread = convo.unread > 0 ? 'unread' : '';
                const badgeHTML = convo.unread > 0 ? `\n<span class="unread-badge">\${convo.unread}</span>` : '';
                const onlineHTML = convo.online ? `\n<span class="online-indicator visible"></span>` : '';

                // Note: Escaped \${} to prevent JSP trying to parse them
                const html = `
                    <div class="conversation-item \${isActive} \${isUnread}" onclick="loadChat(\${convo.id})">
                        <div class="conversation-avatar" style="background: \${convo.color}">
                            \${convo.avatar}
                            \${onlineHTML}
                        </div>
                        <div class="conversation-details">
                            <div class="conversation-header">
                                <span class="conversation-name">\${convo.name}</span>
                                <span class="conversation-time">\${lastMsg.time}</span>
                            </div>
                            <p class="conversation-preview">\${lastMsg.text}</p>
                        </div>
                        \${badgeHTML}
                    </div>
                `;
                container.innerHTML += html;
            });
        }

        function loadChat(id) {
            activeChatId = id;
            const convo = conversations.find(c => c.id === id);
            
            // UI Updates
            document.getElementById('emptyState').style.display = 'none';
            document.getElementById('chatWindow').style.display = 'flex';
            
            // Header
            document.getElementById('headerName').innerText = convo.name;
            document.getElementById('headerAvatar').innerText = convo.avatar;
            document.getElementById('headerAvatar').style.background = convo.color;
            
            // Mark as read logic (visual only)
            convo.unread = 0; 
            renderSidebar(); 

            // Messages
            const msgContainer = document.getElementById('messagesContainer');
            msgContainer.innerHTML = '';
            
            // Date separator
            msgContainer.innerHTML += `<div class="message-group"><div class="message-date">Today</div></div>`;

            convo.messages.forEach(msg => {
                const avatar = msg.type === 'sent' ? 'You' : convo.avatar;
                const bg = msg.type === 'sent' ? '#2563eb' : convo.color;
                
                const html = `
                    <div class="message \${msg.type}">
                        <div class="message-avatar-small" style="background: \${bg}">\${avatar}</div>
                        <div class="message-content">
                            <div class="message-bubble">\${msg.text}</div>
                            <div class="message-time">\${msg.time}</div>
                        </div>
                    </div>
                `;
                msgContainer.innerHTML += html;
            });

            // Scroll to bottom
            msgContainer.scrollTop = msgContainer.scrollHeight;

            // Mobile Handling
            if (window.innerWidth <= 768) {
                document.getElementById('sidebarList').classList.add('hidden');
                document.getElementById('chatWindow').classList.add('active');
            }
        }

        // --- 3. INTERACTION FUNCTIONS ---

        function sendMessage() {
            const input = document.getElementById('messageInput');
            const text = input.value.trim();
            if (!text || !activeChatId) return;

            const convo = conversations.find(c => c.id === activeChatId);
            const time = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

            // Add to data model
            convo.messages.push({
                id: Date.now(),
                sender: "You",
                text: text,
                time: time,
                type: "sent"
            });

            // Re-render
            loadChat(activeChatId);
            input.value = '';
        }

        function handleEnter(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        }

        function filterConversations() {
            const query = document.getElementById('searchInput').value.toLowerCase();
            const items = document.querySelectorAll('.conversation-item');
            
            items.forEach(item => {
                const name = item.querySelector('.conversation-name').innerText.toLowerCase();
                if (name.includes(query)) {
                    item.style.display = 'flex';
                } else {
                    item.style.display = 'none';
                }
            });
        }

        // --- 4. MOBILE UTILITIES ---

        function closeChatMobile() {
            document.getElementById('sidebarList').classList.remove('hidden');
            document.getElementById('chatWindow').classList.remove('active');
            activeChatId = null; // Optional: deselect chat
            document.getElementById('chatWindow').style.display = 'none'; // Hide on desktop too if desired
            document.getElementById('emptyState').style.display = 'none'; // Keep hidden on mobile
            if(window.innerWidth > 768) document.getElementById('emptyState').style.display = 'flex';
        }

        // Initial Render
        renderSidebar();
    </script>
</body>
</html>
