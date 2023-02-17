package com.pedro.event.ringBuffer;

import com.pedro.event.common.enums.PedroEventPlaneExceptionEnum;
import com.pedro.event.common.enums.ProviderTypeEnum;
import com.pedro.event.common.exception.PedroEventPlaneException;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 环形缓冲区抽象框架
 * T 消息类型
 */
public abstract class RingBuffer<T> {

    /**
     * buffer大小
     */
    protected int bufferSize;

    /**
     * buffer数组
     */
    protected ArrayList<T> buffer;


    /**
     * 推送单个消息
     */
    abstract void publish(T message);

    /**
     * 消费单个消息
     */
    abstract T consume();

    /**
     * 尝试推送单个消息
     */
    abstract boolean tryPublish();

    /**
     * 尝试消费单个消息
     */
    abstract Optional<T> tryConsume();

    /**
     * @param messageType      消息类型
     * @param bufferSize       buffer大小
     * @param providerTypeEnum 生产者模式
     * @param <E>              类型
     * @return
     */
    public static <E> RingBuffer<E> createRingBuffer(Class<E> messageType, int bufferSize, ProviderTypeEnum providerTypeEnum) {
        if (providerTypeEnum == ProviderTypeEnum.MULTIPLE_PROVIDER) {
            return new MultiRingBuffer<>(bufferSize);
        } else if (providerTypeEnum == ProviderTypeEnum.SINGLE_PROVIDER) {
            return new SingleRingBuffer<>(bufferSize);
        }

        throw new PedroEventPlaneException(PedroEventPlaneExceptionEnum.PEDRO_EVENT_PLANE_ERROR);
    }

}
