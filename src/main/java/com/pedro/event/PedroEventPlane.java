package com.pedro.event;

import com.pedro.event.common.enums.ConsumerWeightStrategyEnum;
import com.pedro.event.common.enums.PedroEventPlaneExceptionEnum;
import com.pedro.event.common.enums.ProviderTypeEnum;
import com.pedro.event.common.enums.ProviderWeightStrategyEnum;
import com.pedro.event.common.exception.PedroEventPlaneException;
import com.pedro.event.interfaces.Consumer;
import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.ringBuffer.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * 队列主体类
 * T 消息类型
 */
public class PedroEventPlane<T> {

    private static final Logger logger = LoggerFactory.getLogger(PedroEventPlane.class);

    /**
     * 消费线程池
     */
    private ExecutorService executor;

    /**
     * 环形缓冲区
     */
    private RingBuffer<T> ringBuffer;

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
     * 完整构造函数
     *
     * @param executor               消费者执行器
     * @param messageFactory         消息工厂
     * @param messageType            消息类型
     * @param size                   ringBuffer大小
     * @param providerType           生产者生产模式
     * @param consumer               消费函数
     * @param providerWeightStrategy 生产者等待类型
     * @param consumerWeightStrategy 消费者等待类型
     */
    public PedroEventPlane(ExecutorService executor, MessageFactory<T> messageFactory, Class<T> messageType, int size, ProviderTypeEnum providerType, Consumer<T> consumer, ProviderWeightStrategyEnum providerWeightStrategy, ConsumerWeightStrategyEnum consumerWeightStrategy) {
        this.executor = executor;
        if (size < 1 || Integer.bitCount(size) != 1) {
            logger.warn("[pedroEventPlane]pedroEventPlane初始参数异常,size={}", size);
            throw new PedroEventPlaneException(PedroEventPlaneExceptionEnum.PEDRO_EVENT_PLANE_ILLEGAL_ARGS_ERROR);
        }
        this.ringBuffer = RingBuffer.createRingBuffer(messageFactory, size, providerType);
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
     * 向队列中发送单个消息
     *
     * @param message 消息
     */
    public void sendMessage(T message) {

    }

    /**
     * 尝试推送单个消息
     * @param message 消息
     * @return 推送是否成功
     */
    public boolean trySendMessage(T message) {

        return false;
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
    public String pedroEventPlaneInfo() {
        return "0";
    }

}
