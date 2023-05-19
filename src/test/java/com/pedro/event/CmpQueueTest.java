package com.pedro.event;

import com.pedro.event.common.enums.ProviderTypeEnum;
import com.pedro.event.common.enums.ProviderWaitStrategyEnum;
import com.pedro.event.interfaces.Handler;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CmpQueueTest extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(CmpQueueTest.class);

    public void testCmpArrayBlockingQueue() throws InterruptedException {
        // ArrayBlockingQueue
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(16);

        // test1
        logger.info("startA");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ConsumerA consumerA = new ConsumerA();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 不断向线程池提交任务，要求消费消息
                while (true) {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            String message = null;
                            try {
                                message = queue.take();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            consumerA.handle(message);
                        }
                    });
                }
            }
        }).start();
        for (int i = 0; i < 50; i++) {
            queue.put("message" + i);
        }
        Thread.sleep(20000);

    }

    public void testPlane() throws InterruptedException {
        // pedroEventPlane
        PedroEventPlane<String> plane = new PedroEventPlane
                .Builder<String>(Executors.newFixedThreadPool(10), () -> "", 16, ProviderTypeEnum.SINGLE_PROVIDER, new Consumer())
                .providerWeightStrategy(ProviderWaitStrategyEnum.PROVIDER_PARK_1NS_STRATEGY)
                .build();

        // test2
        logger.info("startB");
        plane.start();
        for (int i = 0; i < 50; i++) {
            plane.sendMessage("message" + i);
        }
        Thread.sleep(20000);
    }
}

class ConsumerA implements Handler<String> {


    private static final Logger logger = LoggerFactory.getLogger(ConsumerA.class);

    @Override
    public void handle(String message) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (message.equals("message49")) {
            logger.info("consume end + {}", message);
        }
    }
}