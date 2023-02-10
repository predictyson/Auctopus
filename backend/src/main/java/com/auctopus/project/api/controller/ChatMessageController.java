package com.auctopus.project.api.controller;

import com.auctopus.project.api.request.MessageWriteRequest;
import com.auctopus.project.api.service.LiveService;
import com.auctopus.project.api.service.RedisPublisher;
import com.auctopus.project.api.service.UserService;
import com.auctopus.project.db.domain.LiveChat;
import com.auctopus.project.db.domain.User;
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
    UserService userService;

    @Autowired
    LiveService liveService;

    /**
     * Websocket을 통해 "/pub/chat/message"로 오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void writeMessage(Authentication authentication,
            @RequestBody MessageWriteRequest req) {
        String userEmail = (String) authentication.getCredentials();
        User user = userService.getUser(userEmail);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        String msg = req.getMessage();
        if (req.getType() == 0) {
            msg = "[System] : " + user.getNickname() + "님이 입장하셨습니다.";
        } else if (req.getType() == 1) {
            msg = user.getNickname() + " : " + msg;
        } else if (req.getType() == 2) {
            msg = "[System] : " + user.getNickname() + "님이 " + msg + "원에 입찰하였습니다.";
        }
        LiveChat liveChat = LiveChat.builder()
                .liveSeq(req.getLiveSeq())
                .userEmail(userEmail)
                .nickname(user.getNickname())
                .message(req.getMessage())
                .date(currentTime)
                .type(req.getType())
                .build();
        // Websocket에 발행된 메시지를 redis로 publish한다
        redisPublisher.publish(liveService.getTopic(req.getLiveSeq()), liveChat);

    }
}
