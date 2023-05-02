package com.pedro.event.util;

import java.util.concurrent.locks.LockSupport;

/**
 * 等待策略实现静态工具方法
 * 并非所有的等待策略都可借助此方法实现
 */
public class WaitStrategyUtil {

    /**
     * 等待1ns
     */
    public static void park1ns() {
        LockSupport.parkNanos(1);
    }
}
