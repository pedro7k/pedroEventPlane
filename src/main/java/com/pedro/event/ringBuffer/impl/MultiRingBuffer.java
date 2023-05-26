package com.pedro.event.ringBuffer.impl;

import com.pedro.event.common.enums.FieldStateEnum;
import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.ringBuffer.Container;
import com.pedro.event.ringBuffer.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 多生产者RingBuffer实现
 * T 消息类型
 */
public class MultiRingBuffer<T> extends RingBuffer<T> {

    private static final Logger logger = LoggerFactory.getLogger(MultiRingBuffer.class);

    public MultiRingBuffer(int size, MessageFactory<T> messageFactory) {
        super(size, messageFactory);
    }

    @Override
    public void publish(T message) {
        do {
            // 1.获取当前读写指针
            long currentWP = writePointer.get();
            long currentRP = readPointer.get();

            // 2.尝试写
            if (currentWP - size >= currentRP) {
                // 2.1 写指针领先超过一圈，调用生产者等待
                waitForProvide();
                /// logger.debug("[pedroEventPlane]publish，生产者等待a | wp={},rp={},size={}", currentWP, currentRP, size);
            } else if (writePointer.compareAndSet(currentWP, currentWP + 1)) {
                // 2.2 写指针自增成功，检查当前栅格状态，要求是空或已读
                while (fieldStateHolder.getItem(currentWP) == FieldStateEnum.WRITTEN_FIELD) {
                    waitForProvide();
                    // logger.debug("[pedroEventPlane]publish，生产者等待b");
                }
                // 2.3 写对象
                container.putItem(currentWP, message);
                // 2.4 更新标识
                fieldStateHolder.putItem(currentWP, FieldStateEnum.WRITTEN_FIELD);
                logger.info("[pedroEventPlane]publish成功, 写指针值为{}", currentWP);
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
                // 2.1 读指针不落后于写指针，调用消费者等待
                waitForConsume();
                // logger.debug("[pedroEventPlane]consume，消费者等待a | wp={},rp={},size={}", currentWP, currentRP, size);
            } else if (readPointer.compareAndSet(currentRP, currentRP + 1)) {
                // 2.2 读指针自增成功，检查当前栅格状态，要求是已写
                while (fieldStateHolder.getItem(currentRP) != FieldStateEnum.WRITTEN_FIELD) {
                    waitForConsume();
                    // logger.debug("[pedroEventPlane]consume，消费者等待b");
                }
                // 2.3 读对象
                T message = container.getItem(currentRP);
                // 2.4 更新标识
                fieldStateHolder.putItem(currentRP, FieldStateEnum.READ_FIELD);
                logger.info("[pedroEventPlane]consume成功, 读指针值为{}", currentRP);
                // 2.5 返回
                return message;
            }
        } while (true);
    }

    @Override
    public boolean tryPublish(T message) {
        // 1.获取当前写指针
        long currentWP = writePointer.get();
        long currentRP = readPointer.get();

        // 2.尝试写
        if (currentWP - size >= currentRP) {
            // 2.1 写指针领先超过一圈，返回失败
            return false;
        } else if (writePointer.compareAndSet(currentWP, currentWP + 1)) {
            // 2.2 检查当前栅格状态，要求是空或已读，再自增写指针
            if (fieldStateHolder.getItem(currentWP) != FieldStateEnum.WRITTEN_FIELD) {
                return false;
            }
            // 2.3 写对象
            container.putItem(currentWP, message);
            // 2.4 更新标识
            fieldStateHolder.putItem(currentWP, FieldStateEnum.WRITTEN_FIELD);
            logger.info("[pedroEventPlane]tryPublish成功, 写指针值为{}", currentWP);
            // 2.5 返回成功
            return true;
        }

        // 3.失败
        return false ;
    }

    @Override
    public Optional<T> tryConsume() {

        // 1. 获取当前读指针
        long currentWP = writePointer.get();
        long currentRP = readPointer.get();

        // 2.尝试读
        if (currentRP >= currentWP) {
            // 2.1 读指针不落后于写指针，返回失败
            return Optional.empty();
        } else if (readPointer.compareAndSet(currentRP, currentRP + 1)) {
            // 2.2 读指针自增成功，检查当前栅格状态，要求是已写
            do {
                if (fieldStateHolder.getItem(currentRP) == FieldStateEnum.WRITTEN_FIELD) {
                    break;
                }
            } while (true);
            // 2.3 读对象
            T message = container.getItem(currentRP);
            // 2.4 更新标识
            fieldStateHolder.putItem(currentRP, FieldStateEnum.READ_FIELD);
            logger.info("[pedroEventPlane]tryConsume成功, 读指针值为{}", currentRP);
            // 2.5 返回
            return Optional.of(message);
        }

        // 3.返回失败
        return Optional.empty();
    }
}
