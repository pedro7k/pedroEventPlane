package com.pedro.event.ringBuffer;

import java.util.Optional;

/**
 * 环形缓冲区
 * T 消息类型
 */
public interface RingBuffer<T> {

    /**
     * 推送单个消息
     */
    void publish(T message);

    /**
     * 消费单个消息
     */
    T consume();

    /**
     * 尝试推送单个消息
     */
    boolean tryPublish();

    /**
     * 尝试消费单个消息
     */
    Optional<T> tryConsume();


}
