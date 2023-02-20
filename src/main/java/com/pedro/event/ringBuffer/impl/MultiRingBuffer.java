package com.pedro.event.ringBuffer.impl;

import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.ringBuffer.Container;
import com.pedro.event.ringBuffer.RingBuffer;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 多生产者RingBuffer实现
 * T 消息类型
 */
public class MultiRingBuffer<T> extends RingBuffer<T> {

    public MultiRingBuffer(int size, MessageFactory<T> messageFactory) {
        super(size, messageFactory);
    }

    @Override
    public void publish(T message) {

    }

    @Override
    public T consume() {
        return null;
    }

    @Override
    public boolean tryPublish(T message) {
        return false;
    }

    @Override
    public Optional<T> tryConsume() {
        return Optional.empty();
    }
}
