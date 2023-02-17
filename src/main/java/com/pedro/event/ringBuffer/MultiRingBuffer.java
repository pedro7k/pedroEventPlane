package com.pedro.event.ringBuffer;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 多生产者RingBuffer实现
 * T 消息类型
 */
public class MultiRingBuffer<T> extends RingBuffer<T>{

    public MultiRingBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        this.buffer = new ArrayList<>(bufferSize);
    }

    @Override
    void publish(T message) {

    }

    @Override
    T consume() {
        return null;
    }

    @Override
    boolean tryPublish() {
        return false;
    }

    @Override
    Optional<T> tryConsume() {
        return Optional.empty();
    }
}
