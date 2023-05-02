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
            // 1.获取当前写指针
            long currentWP = writePointer.get();
            long currentRP = readPointer.get();

            // 2.尝试写
            if (writePointer.get() - size >= currentRP) {
                // 2.1 调用生产者等待
                waitForProvide();
            } else if (writePointer.compareAndSet(currentWP, currentWP + 1)) { // TODO cas和下一步操作之间的读写并发
                // 2.2 写指针自增成功，向Container写对象
                container.putItem(currentWP, message);
                // TODO 2.3 更新flag
                return;
            }
        } while (true);

    }

    @Override
    public T consume() {
        do {
            // 1. 获取当前读指针
            long currentWP = writePointer.get();
            long currentRP = readPointer.get();

            // 2.尝试读
            if (currentRP >= currentWP) {
                // 2.1 调用消费者等待
                waitForConsume();
            } else if (readPointer.compareAndSet(currentRP, currentRP + 1)) { // TODO cas和下一步操作之间的读写并发
                // 2.2 读指针自增成功，从Container读对象
                return container.getItem(currentRP);
                // TODO 2.3 更新flag
            }
        } while (true);
    }

    @Override
    public boolean tryPublish(T message) {
        // 1.获取当前写指针
        long currentWP = writePointer.get();
        if (writePointer.get() - size >= readPointer.get()) {
            // 2.1 返回失败
            return false;
        } else if (writePointer.compareAndSet(currentWP, currentWP + 1)) {
            // 2.2 写指针自增成功，向Container写对象
            container.putItem(currentWP, message);
            return true;
        }

        // 3.返回失败
        return false;
    }

    @Override
    public Optional<T> tryConsume() {
        return Optional.empty();
    }
}
