package com.auctopus.project.api.controller;

import com.auctopus.project.api.request.MessageWriteRequest;
import com.auctopus.project.api.service.LiveService;
import com.auctopus.project.api.service.RedisPublisher;
import com.auctopus.project.db.domain.LiveChat;
import com.auctopus.project.db.domain.LiveChat.MessageType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final RedisPublisher redisPublisher;

    @Autowired
    LiveService liveService;

    /**
     * Websocket을 통해 "/pub/chat/message"로 오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void writeMessage(Authentication authentication,
            @RequestBody MessageWriteRequest req) {
        String userEmail = (String) authentication.getCredentials();
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        LiveChat liveChat = LiveChat.builder()
                .liveSeq(req.getLiveSeq())
                .userEmail(userEmail)
                .message(req.getMessage())
                .date(currentTime)
                .type(MessageType.ENTER)
                .build();
        redisPublisher.publish(liveService.getTopic(req.getLiveSeq()), liveChat);

    }
}
