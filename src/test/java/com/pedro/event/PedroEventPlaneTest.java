package com.pedro.event;

import com.pedro.event.common.enums.ProviderTypeEnum;
import com.pedro.event.common.enums.ProviderWaitStrategyEnum;
import com.pedro.event.interfaces.Handler;
import junit.framework.TestCase;

import java.util.concurrent.Executors;

/**
 * 测试
 */
public class PedroEventPlaneTest extends TestCase {

    public void test() {
        PedroEventPlane<String> plane = new PedroEventPlane
                .Builder<String>(Executors.newFixedThreadPool(2), () -> "", 4, ProviderTypeEnum.SINGLE_PROVIDER, new Consumer())
                .providerWeightStrategy(ProviderWaitStrategyEnum.PROVIDER_PARK_1NS_STRATEGY)
                .build();

        plane.start();

        for (int i = 0; i < 100; i++) {
            plane.sendMessage("message" + i);
        }
    }
}

class Consumer implements Handler<String> {

    @Override
    public void handle(String message) {
        System.out.println("consume + " + message);
    }
}

