package com.soulspace.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.soulspace.dto.ChatMessage;
import com.soulspace.model.User;
import com.soulspace.service.MessageService;
import com.soulspace.service.UserService;

@Controller
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserService userService;

    public WebSocketChatController(SimpMessagingTemplate messagingTemplate,
            MessageService messageService,
            UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userService = userService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage chatMessage) {
        // 1. Save to Database
        messageService.sendMessage(chatMessage.getSenderId(), chatMessage.getReceiverId(), chatMessage.getContent());

        // 2. Prepare for Broadcast
        User sender = userService.getUserById(chatMessage.getSenderId());
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        chatMessage.setTimestamp(time);
        chatMessage.setSenderName(sender.getFirstName() + " " + sender.getLastName());

        // 3. Send to both users' private queues
        // Recipient
        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverId().toString(), "/queue/messages", chatMessage);

        // Sender (to sync across tabs)
        messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId().toString(), "/queue/messages", chatMessage);
    }
}
