package com.auctopus.project.api.service;

import com.auctopus.project.db.domain.LiveChat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private ObjectMapper objectMapper;
    private RedisTemplate redisTemplate;
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessag가 해당 메시지를 받아 모두에게 보낸다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer()
                    .deserialize(message.getBody());
            System.out.println("publicMessage " + publishMessage);
            // LiveChat 객체로 매핑
            LiveChat liveChat = objectMapper.readValue(publishMessage, LiveChat.class);
            // Websocket 구독자에게 채팅 메시지 보내기
            messagingTemplate.convertAndSend("sub/chat/room" + liveChat.getLiveSeq(), liveChat);
        } catch (Exception e) {
//            throw new ChatMessageNotFoundException();
            log.error(e.getMessage());
        }

    }
}
