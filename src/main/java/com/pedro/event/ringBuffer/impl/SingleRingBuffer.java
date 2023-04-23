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

        do {
            long current = writePointer.get();
            if (writePointer.get() - size <= readPointer.get()) {
                waitForProvide();
            } else if (writePointer.compareAndSet(current, current + 1)) {
                container.putItem(current, message);
            }
        } while (true);

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
