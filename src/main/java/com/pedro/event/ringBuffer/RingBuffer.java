package com.pedro.event.ringBuffer;

import com.pedro.event.common.enums.*;
import com.pedro.event.common.exception.PedroEventPlaneException;
import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.ringBuffer.impl.MultiRingBuffer;
import com.pedro.event.ringBuffer.impl.SingleRingBuffer;
import com.pedro.event.util.WaitStrategyUtil;

import java.util.Optional;
import java.util.concurrent.locks.LockSupport;


/**
 * 缓冲区属性，统一填充不会被更改的属性
 *
 * @param <T> 消息类型
 */
abstract class RingBufferField<T> {

    protected long p1, p2, p3, p4, p5, p6, p7;

    /**
     * buffer大小
     */
    protected int size;

    protected long p8, p9, p10, p11, p12, p13, p14, p15;

}

/**
 * 环形缓冲区抽象框架
 * T 消息类型
 */
public abstract class RingBuffer<T> extends RingBufferField<T> {

    /**
     * 消息容器
     */
    protected Container<T> container;

    /**
     * 状态标识容器
     */
    protected Container<FieldStateEnum> fieldStateHolder;

    /**
     * 写主指针
     */
    protected Pointer writePointer = new Pointer();

    /**
     * 读主指针
     */
    protected Pointer readPointer = new Pointer();

    /**
     * 生产者等待策略
     */
    private ProviderWaitStrategyEnum providerWaitStrategy = ProviderWaitStrategyEnum.PROVIDER_PARK_1NS_STRATEGY;

    /**
     * 消费者等待策略
     */
    private ConsumerWaitStrategyEnum consumerWaitStrategy = ConsumerWaitStrategyEnum.CONSUMER_PARK_1NS_STRATEGY;

    /**
     * 父类构造器
     *
     * @param size           buffer大小
     * @param messageFactory 空消息工厂
     */
    public RingBuffer(int size, MessageFactory<T> messageFactory) {
        this.size = size;
        this.container = new Container<>(size, messageFactory);
        this.fieldStateHolder = new Container<>(size, () -> FieldStateEnum.NULL_FIELD);
    }

    /**
     * 推送单个消息
     *
     * @param message 消息
     */
    public abstract void publish(T message);

    /**
     * 消费单个消息
     *
     * @return 消费结果
     */
    public abstract T consume();

    /**
     * 尝试推送单个消息
     *
     * @param message 消息
     * @return 生产是否成功
     */
    public abstract boolean tryPublish(T message);

    /**
     * 尝试消费单个消息
     *
     * @return 消费是否成功，若成功得到结果
     */
    public abstract Optional<T> tryConsume();

    /**
     * @param messageFactory   空消息工厂
     * @param size             buffer大小
     * @param providerTypeEnum 生产者模式
     * @param <E>              类型
     * @return RingBuffer实例
     */
    public static <E> RingBuffer<E> createRingBuffer(MessageFactory<E> messageFactory, int size, ProviderTypeEnum providerTypeEnum) {
        if (providerTypeEnum == ProviderTypeEnum.MULTIPLE_PROVIDER) {
            return new MultiRingBuffer<>(size, messageFactory);
        } else if (providerTypeEnum == ProviderTypeEnum.SINGLE_PROVIDER) {
            return new SingleRingBuffer<>(size, messageFactory);
        }

        throw new PedroEventPlaneException(PedroEventPlaneExceptionEnum.PEDRO_EVENT_PLANE_ERROR);
    }

    /**
     * 调用生产者等待
     */
    protected void waitForProvide() {
        if (providerWaitStrategy == ProviderWaitStrategyEnum.PROVIDER_PARK_1NS_STRATEGY) {
            // 等待1ns
            WaitStrategyUtil.park1ns();
        }
    }

    /**
     * 调用消费者等待
     */
    protected void waitForConsume() {
        if (consumerWaitStrategy == ConsumerWaitStrategyEnum.CONSUMER_PARK_1NS_STRATEGY) {
            // 等待1ns
            WaitStrategyUtil.park1ns();
        }
    }

    public ProviderWaitStrategyEnum getProviderWaitStrategy() {
        return providerWaitStrategy;
    }

    public void setProviderWaitStrategy(ProviderWaitStrategyEnum providerWaitStrategy) {
        this.providerWaitStrategy = providerWaitStrategy;
    }

    public ConsumerWaitStrategyEnum getConsumerWaitStrategy() {
        return consumerWaitStrategy;
    }

    public void setConsumerWaitStrategy(ConsumerWaitStrategyEnum consumerWaitStrategy) {
        this.consumerWaitStrategy = consumerWaitStrategy;
    }

    public Container<T> getContainer() {
        return container;
    }

    public void setContainer(Container<T> container) {
        this.container = container;
    }
}
