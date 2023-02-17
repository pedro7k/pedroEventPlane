package com.pedro.event;

import com.pedro.event.common.enums.ConsumerWeightStrategyEnum;
import com.pedro.event.common.enums.ProviderTypeEnum;
import com.pedro.event.common.enums.ProviderWeightStrategyEnum;
import com.pedro.event.interfaces.Consumer;
import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.ringBuffer.RingBufferWrapper;

import java.util.concurrent.ExecutorService;

/**
 * 队列主体类
 * T 消息类型
 */
public class PedroEventPlane<T> {

    /**
     * 消费线程池
     */
    private ExecutorService executor;

    /**
     * 消息工厂-初始化填充buffer
     */
    private MessageFactory<T> messageFactory;

    /**
     * 环形缓冲区
     */
    private RingBufferWrapper<T> ringBuffer;

    /**
     * 单/多生产者模式
     */
    private ProviderTypeEnum providerType;

    /**
     * 消费函数
     */
    private Consumer<T> consumer;

    /**
     * 生产者等待策略
     */
    private ProviderWeightStrategyEnum providerWeightStrategy;
    private final ProviderWeightStrategyEnum defaultProviderWeightStrategy = ProviderWeightStrategyEnum.PARK_1S_STRATEGY;

    /**
     * 消费者等待策略
     */
    private ConsumerWeightStrategyEnum consumerWeightStrategy;
    private final ConsumerWeightStrategyEnum defaultConsumerWeightStrategy = ConsumerWeightStrategyEnum.PARK_1S_STRATEGY;



    /**
     * 构造函数
     *
     * @param executor       消费线程池
     * @param messageFactory 消息工厂-初始化填充buffer
     * @param ringBufferSize 环形缓冲区大小
     * @param providerType   单/多生产者模式
     * @param consumer       消费函数
     */
    public PedroEventPlane(ExecutorService executor, MessageFactory<T> messageFactory, int ringBufferSize, ProviderTypeEnum providerType, Consumer<T> consumer, ProviderWeightStrategyEnum providerWeightStrategy, ConsumerWeightStrategyEnum consumerWeightStrategy) {
        this.executor = executor;
        this.messageFactory = messageFactory;
        this.ringBuffer = ringBuffer;
        this.providerType = providerType;
        this.consumer = consumer;
        this.providerWeightStrategy = providerWeightStrategy;
        this.consumerWeightStrategy = consumerWeightStrategy;
    }

    /**
     * 队列启动
     */
    public void start() {

    }

    /**
     * 向队列中发送消息
     *
     * @param message 消息
     */
    public void sendMessage(T message) {

    }

    /**
     * 立即关闭队列
     */
    public void shutdown() {

    }

    /**
     * 关闭队列，若有消息未消费完，允许等待一定时长
     *
     * @param timeout 超时时间
     */
    public void shutdown(Long timeout) {

    }

    /**
     * 队列状态监控
     *
     * @return 未消费消息数量
     */
    public String waitingMessageCount() {
        return "0";
    }


}
