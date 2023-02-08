package com.auctopus.project.api.service;

import com.auctopus.project.db.domain.LiveChat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, LiveChat message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
