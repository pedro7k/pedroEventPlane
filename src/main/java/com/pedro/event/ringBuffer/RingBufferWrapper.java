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
public class RingBufferWrapper<T> implements RingBuffer<T> {

    private final RingBuffer<T> ringBuffer;

    private final int bufferSize;

    private ArrayList<T> buffer;

    public RingBufferWrapper(int bufferSize, ProviderTypeEnum providerTypeEnum) {
        this.bufferSize = bufferSize;
        this.buffer = new ArrayList<>(bufferSize);
        this.ringBuffer = createRingBuffer(providerTypeEnum);
    }

    @Override
    public void publish(T message) {
        ringBuffer.publish(message);
    }

    @Override
    public T consume() {
        return ringBuffer.consume();
    }

    @Override
    public boolean tryPublish() {
        return ringBuffer.tryPublish();
    }

    @Override
    public Optional<T> tryConsume() {
        return ringBuffer.tryConsume();
    }


    final class MultiRingBuffer<T> implements RingBuffer<T> {

        @Override
        public void publish(T message) {
        }

        @Override
        public T consume() {
            return null;
        }

        @Override
        public boolean tryPublish() {
            return false;
        }

        @Override
        public Optional<T> tryConsume() {
            return Optional.empty();
        }
    }

    final class SingleRingBuffer<T> implements RingBuffer<T> {

        @Override
        public void publish(T message) {

        }

        @Override
        public T consume() {
            return null;
        }

        @Override
        public boolean tryPublish() {
            return false;
        }

        @Override
        public Optional<T> tryConsume() {
            return Optional.empty();
        }
    }

    /**
     * 创建RingBuffer实现
     *
     * @param providerTypeEnum 生产者类型
     * @return RingBuffer
     */
    public RingBuffer<T> createRingBuffer(ProviderTypeEnum providerTypeEnum) {
        if (providerTypeEnum == ProviderTypeEnum.MULTIPLE_PROVIDER) {
            return new MultiRingBuffer<>();
        } else if (providerTypeEnum == ProviderTypeEnum.SINGLE_PROVIDER) {
            return new SingleRingBuffer<>();
        }

        throw new PedroEventPlaneException(PedroEventPlaneExceptionEnum.PEDRO_EVENT_PLANE_ERROR);
    }

}
