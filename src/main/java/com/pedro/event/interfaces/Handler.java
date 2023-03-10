package com.pedro.event.interfaces;

/**
 * 消费者
 * T 消息类型
 */
public interface Handler<T> {

    void handle(T message);
}
