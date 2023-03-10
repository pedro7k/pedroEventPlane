package com.pedro.event.ringBuffer.impl;

import com.pedro.event.common.enums.ProviderTypeEnum;
import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.ringBuffer.Container;
import com.pedro.event.ringBuffer.RingBuffer;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 单生产者RingBuffer实现
 * T 消息类型
 */
public class SingleRingBuffer<T> extends RingBuffer<T> {

    public SingleRingBuffer(int size, MessageFactory<T> messageFactory) {
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
