<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoulSpace - ${botName}</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f9fafb;
            height: 100vh;
            overflow: hidden; 
        }

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

        .chat-container {
            flex: 1;
            display: flex;
            flex-direction: column;
            background: white;
            position: relative;
            overflow: hidden; 
        }

        .chat-header { 
            background: linear-gradient(135deg, #3b82f6, #9333ea); 
            color: white; 
            padding: 1rem 1.5rem; 
            display: flex; 
            align-items: center; 
            justify-content: space-between; 
            flex-shrink: 0; 
        }
        
        .chat-messages { flex: 1; overflow-y: auto; padding: 1.5rem; background: #f9fafb; }
        
        /* Message Bubbles */
        .message { display: flex; gap: 0.75rem; margin-bottom: 1.5rem; animation: fadeIn 0.3s ease; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
        
        .message.user { flex-direction: row-reverse; }
        
        .message-avatar { width: 2.5rem; height: 2.5rem; border-radius: 50%; flex-shrink: 0; display: flex; align-items: center; justify-content: center; color: white; }
        .message-avatar.bot { background: linear-gradient(135deg, #3b82f6, #9333ea); }
        .message-avatar.user { background: #6b7280; }
        
        .message-content { max-width: 70%; }
        .message.user .message-content { display: flex; flex-direction: column; align-items: flex-end; }
        
        .message-bubble { padding: 0.75rem 1rem; border-radius: 1rem; background: white; box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1); }
        .message.user .message-bubble { background: #2563eb; color: white; border-bottom-right-radius: 0.25rem; }
        .message.bot .message-bubble { border-bottom-left-radius: 0.25rem; }
        
        /* Input Area */
        .chat-input-container { padding: 1rem 1.5rem; background: white; border-top: 1px solid #e5e7eb; flex-shrink: 0; }
        .chat-input-wrapper { display: flex; gap: 0.75rem; align-items: flex-end; }
        .chat-input { flex: 1; padding: 0.75rem 1rem; border: 1px solid #d1d5db; border-radius: 1.5rem; font-size: 1rem; resize: none; max-height: 8rem; font-family: inherit; }
        .chat-input:focus { outline: none; border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
        
        .send-btn { width: 3rem; height: 3rem; background: linear-gradient(135deg, #3b82f6, #9333ea); border: none; border-radius: 50%; color: white; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: all 0.2s; flex-shrink: 0; }
        .send-btn:hover:not(:disabled) { transform: scale(1.05); }

        /* Welcome Screen & Cards */
        .welcome-screen { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 2rem; text-align: center; height: 100%; }
        .welcome-icon { width: 5rem; height: 5rem; background: linear-gradient(135deg, #eff6ff, #f3e8ff); border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 1.5rem; }
        .welcome-icon svg { width: 2.5rem; height: 2.5rem; color: #2563eb; }
        
        .suggestion-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; width: 100%; max-width: 40rem; margin-top: 2rem; }
        .suggestion-card { padding: 1rem; background: white; border: 1px solid #e5e7eb; border-radius: 0.75rem; cursor: pointer; transition: all 0.2s; text-align: left; }
        .suggestion-card:hover { border-color: #3b82f6; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); transform: translateY(-2px); }
        .suggestion-card-icon { width: 2rem; height: 2rem; background: #eff6ff; border-radius: 0.5rem; display: flex; align-items: center; justify-content: center; margin-bottom: 0.75rem; }
        .suggestion-card-icon svg { width: 1.25rem; height: 1.25rem; color: #2563eb; }

        /* Mobile Toggle */
        .mobile-menu-btn { display: none; background: none; border: none; cursor: pointer; margin-right: 1rem; color: white; }
        @media (max-width: 1024px) { .mobile-menu-btn { display: block; } }
        
        /* Typing Indicator */
        .typing-indicator { display: none; padding: 0.75rem 1rem; background: white; border-radius: 1rem; border-bottom-left-radius: 0.25rem; width: fit-content; margin-bottom: 1.5rem; }
        .typing-indicator.active { display: flex; gap: 0.25rem; }
        .typing-dot { width: 0.5rem; height: 0.5rem; background: #9ca3af; border-radius: 50%; animation: typing 1.4s infinite; }
        .typing-dot:nth-child(2) { animation-delay: 0.2s; }
        .typing-dot:nth-child(3) { animation-delay: 0.4s; }
        @keyframes typing { 0%, 60%, 100% { transform: translateY(0); } 30% { transform: translateY(-0.5rem); } }
    </style>
</head>
<body>

    <jsp:include page="navigation.jsp" />

    <main class="main-content">
        <div class="chat-container">
            
            <div class="chat-header">
                <div class="chat-header-left" style="display:flex; align-items:center;">
                    <button class="mobile-menu-btn" onclick="openSidebar()">
                         <svg style="width:24px;height:24px" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
                        </svg>
                    </button>
                    <div class="chat-avatar" style="margin-right:0.75rem;">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" style="width:1.5rem;height:1.5rem;">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                        </svg>
                    </div>
                    <div class="chat-info">
                        <h2 style="font-size:1.1rem;font-weight:500;">${botName}</h2>
                        <div style="font-size:0.8rem;opacity:0.9;">Online</div>
                    </div>
                </div>
                <div class="chat-actions">
                    <button class="chat-action-btn" onclick="clearChat()" style="background:rgba(255,255,255,0.2);border:none;border-radius:0.5rem;padding:0.5rem;color:white;cursor:pointer;">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" style="width:1.25rem;height:1.25rem;">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                        </svg>
                    </button>
                </div>
            </div>

            <div class="chat-messages" id="chatMessages">
                <div class="welcome-screen" id="welcomeScreen">
                    <div class="welcome-icon">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" /></svg>
                    </div>
                    <h3 style="margin-bottom:0.5rem;">${welcomeTitle}</h3>
                    <p style="color:#6b7280; max-width:28rem;">${welcomeSubtitle}</p>
                    
                    <div class="suggestion-cards">
                        <c:forEach var="card" items="${suggestionList}">
                            <div class="suggestion-card" onclick="sendSuggestion('${card.actionText}')">
                                <div class="suggestion-card-icon">
                                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="${card.iconPath}" />
                                    </svg>
                                </div>
                                <h4>${card.title}</h4>
                                <p style="font-size:0.75rem;color:#6b7280;">${card.description}</p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="chat-input-container">
                <div class="chat-input-wrapper">
                    <textarea id="chatInput" class="chat-input" placeholder="Type your message..." rows="1" onkeydown="handleKeyPress(event)" oninput="autoResize(this)"></textarea>
                    <button class="send-btn" id="sendBtn" onclick="sendMessage()">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" style="width:1.25rem;height:1.25rem;">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                        </svg>
                    </button>
                </div>
            </div>
        </div>
    </main>

    <script>
        var conversationStarted = false;

        function autoResize(textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = Math.min(textarea.scrollHeight, 128) + 'px';
        }

        function handleKeyPress(event) {
            if (event.key === 'Enter' && !event.shiftKey) {
                event.preventDefault();
                sendMessage();
            }
        }

        function sendMessage() {
            var input = document.getElementById('chatInput');
            var message = input.value.trim();
            if (!message) return;

            if (!conversationStarted) {
                var welcomeScreen = document.getElementById('welcomeScreen');
                if(welcomeScreen) welcomeScreen.style.display = 'none';
                conversationStarted = true;
            }

            addMessage('user', message);
            input.value = '';
            input.style.height = 'auto';
            showTypingIndicator();

            setTimeout(function() {
                hideTypingIndicator();
                var response = getAIResponse(message);
                addMessage('bot', response.text, response.quickReplies);
            }, 1000);
        }

        function sendSuggestion(text) {
            document.getElementById('chatInput').value = text;
            sendMessage();
        }

        function addMessage(type, text, quickReplies) {
            var container = document.getElementById('chatMessages');
            var div = document.createElement('div');
            div.className = 'message ' + type;
            
            var avatarHTML = (type === 'bot') 
                ? '<svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" /></svg>'
                : 'You';

            div.innerHTML = 
                '<div class="message-avatar ' + type + '">' + avatarHTML + '</div>' +
                '<div class="message-content">' +
                    '<div class="message-bubble">' + text + '</div>' +
                '</div>';
            
            if (quickReplies && quickReplies.length > 0) {
                var qrDiv = document.createElement('div');
                qrDiv.style.marginTop = '0.75rem';
                qrDiv.style.display = 'flex';
                qrDiv.style.gap = '0.5rem';
                
                for (var i = 0; i < quickReplies.length; i++) {
                    (function(reply){
                        var btn = document.createElement('button');
                        btn.textContent = reply;
                        btn.style.padding = '0.5rem 1rem';
                        btn.style.borderRadius = '999px';
                        btn.style.border = '1px solid #d1d5db';
                        btn.style.background = 'white';
                        btn.style.cursor = 'pointer';
                        btn.onclick = function() { sendSuggestion(reply); };
                        qrDiv.appendChild(btn);
                    })(quickReplies[i]);
                }
                div.querySelector('.message-content').appendChild(qrDiv);
            }

            container.appendChild(div);
            container.scrollTop = container.scrollHeight;
        }

        function showTypingIndicator() {
            var container = document.getElementById('chatMessages');
            var div = document.createElement('div');
            div.className = 'message bot';
            div.id = 'typingIndicator';
            div.innerHTML = 
                '<div class="message-avatar bot"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" /></svg></div>' +
                '<div class="message-content"><div class="typing-indicator active"><div class="typing-dot"></div><div class="typing-dot"></div><div class="typing-dot"></div></div></div>';
            container.appendChild(div);
            container.scrollTop = container.scrollHeight;
        }

        function hideTypingIndicator() {
            var el = document.getElementById('typingIndicator');
            if(el) el.remove();
        }

        function getAIResponse(msg) {
            var m = msg.toLowerCase();
            if(m.includes('anxious') || m.includes('stress')) {
                return { text: "I hear you. Try a simple breathing exercise: Inhale for 4 seconds, hold for 7, exhale for 8.", quickReplies: ["Try exercise", "Other tips"] };
            }
            // 2. Check for Sleep (Matches "I want to improve my sleep")
            else if (m.includes('sleep') || m.includes('insomnia') || m.includes('tired')) {
                return { 
                    text: "Good sleep is the foundation of mental health. Establishing a routine helps. Have you tried avoiding screens 30 minutes before bed?", 
                    quickReplies: ["Sleep hygiene tips", "Guided sleep meditation", "Relaxation sounds"] 
                };
            }

            // 3. Check for Mindfulness (Matches "Tell me about mindfulness")
            else if (m.includes('mindfulness') || m.includes('meditation') || m.includes('present')) {
                return { 
                    text: "Mindfulness is simply observing the present moment without judgment. It helps ground us. Would you like to try a quick 1-minute practice?", 
                    quickReplies: ["Start 1-min practice", "What are the benefits?", "Daily mindfulness"] 
                };
            }

            // 4. Default Response (If no keywords match)
            return { 
                text: "I'm here to listen. I can help with anxiety, sleep, stress, or mindfulness. What's on your mind right now?", 
                quickReplies: ["I'm feeling anxious", "I can't sleep", "Tell me about mindfulness"] 
            };
        }

        function clearChat() { window.location.reload(); }
    </script>
</body>
</html>