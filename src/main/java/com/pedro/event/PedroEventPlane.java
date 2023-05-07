package com.pedro.event;

import com.pedro.event.common.enums.ConsumerWaitStrategyEnum;
import com.pedro.event.common.enums.PedroEventPlaneExceptionEnum;
import com.pedro.event.common.enums.ProviderTypeEnum;
import com.pedro.event.common.enums.ProviderWaitStrategyEnum;
import com.pedro.event.common.exception.PedroEventPlaneException;
import com.pedro.event.interfaces.Handler;
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
    private final ExecutorService executor;

    /**
     * 环形缓冲区
     */
    private final RingBuffer<T> ringBuffer;

    /**
     * 单/多生产者模式
     */
    private final ProviderTypeEnum providerType;

    /**
     * 消费函数
     */
    private final Handler<T> handler;

    /**
     * 启动标识
     */
    private volatile boolean started = false;


    /**
     * 构造器
     * executor               消费者执行器
     * messageFactory         消息工厂
     * messageType            消息类型
     * size                   ringBuffer大小
     * providerType           生产者生产模式
     * consumer               消费函数
     * providerWeightStrategy 生产者等待类型
     * consumerWeightStrategy 消费者等待类型
     */
    public static class Builder<T> {

        // Required parameters
        private final ExecutorService executor;
        private final RingBuffer<T> ringBuffer;
        private final ProviderTypeEnum providerType;
        private final Handler<T> handler;

        // build method
        public PedroEventPlane<T> build() {
            return new PedroEventPlane<>(this);
        }

        // build constructor
        public Builder(ExecutorService executor, MessageFactory<T> messageFactory, int size, ProviderTypeEnum providerType, Handler<T> handler) {
            this.executor = executor;
            if (size < 1 || Integer.bitCount(size) != 1) {
                // size必须为正的2的幂
                logger.warn("[pedroEventPlane]pedroEventPlane初始参数异常,size={}", size);
                throw new PedroEventPlaneException(PedroEventPlaneExceptionEnum.PEDRO_EVENT_PLANE_ILLEGAL_ARGS_ERROR);
            }
            this.ringBuffer = RingBuffer.createRingBuffer(messageFactory, size, providerType);
            this.providerType = providerType;
            this.handler = handler;
        }

        public Builder<T> providerWeightStrategy(ProviderWaitStrategyEnum providerWaitStrategyEnum) {
            this.ringBuffer.setProviderWaitStrategy(providerWaitStrategyEnum);
            return this;
        }

        public Builder<T> consumerWeightStrategy(ConsumerWaitStrategyEnum consumerWeightStrategy) {
            this.ringBuffer.setConsumerWaitStrategy(consumerWeightStrategy);
            return this;
        }
    }

    /**
     * 私有构造器，供Builder使用
     *
     * @param builder
     */
    private PedroEventPlane(Builder<T> builder) {
        this.executor = builder.executor;
        this.ringBuffer = builder.ringBuffer;
        this.providerType = builder.providerType;
        this.handler = builder.handler;
    }

    /**
     * 队列启动
     */
    public void start() {

        // 1.检测重复启动
        if (started){
            logger.error("[pedroEventPlane]在尝试一次pedroEventPlane重复启动");
            return;
        }

        // 2.修改状态
        started = true;

        // 3.


    }

    /**
     * 向队列中发送单个消息
     *
     * @param message 消息
     */
    public void sendMessage(T message) {
        if (message == null) {
            logger.warn("[pedroEventPlane]传入message为null");
            return;
        }

        ringBuffer.publish(message);
    }

    /**
     * 尝试推送单个消息
     *
     * @param message 消息
     * @return 推送是否成功
     */
    public boolean trySendMessage(T message) {
        if (message == null) {
            logger.warn("[pedroEventPlane]传入message为null");
            return false;
        }

        return ringBuffer.tryPublish(message);
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

    /**
     * Getter of provider type
     */
    public ProviderTypeEnum getProviderType() {
        return providerType;
    }
}
