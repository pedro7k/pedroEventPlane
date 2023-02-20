package com.pedro.event.ringBuffer;

import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.util.UnsafeUtil;
import sun.misc.Unsafe;

import java.util.ArrayList;

/**
 * 环形缓冲区的实际对象容器
 */
abstract class ContainerField<T> {

    protected long p1, p2, p3, p4, p5, p6, p7;

    /**
     * 元素数组
     */
    protected T[] buffer;

    protected long p8, p9, p10, p11, p12, p13, p14, p15;

    /**
     * Container大小
     */
    protected int size;

    /**
     * UNSAFE
     */
    protected static final Unsafe U = UnsafeUtil.getUnsafe();

    protected long p16, p17, p18, p19, p20, p21, p22, p23;

}

public class Container<T> extends ContainerField<T> {

    /**
     * 构造器
     * @param size buffer大小
     * @param messageFactory 消息工厂
     */
    public Container(int size, MessageFactory<T> messageFactory) {
        // 1.属性填充
        this.buffer = (T[]) new Object[size];
        this.size = size;
        // 2.预填充对象
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = messageFactory.createEmptyMessage();
        }
    }

    /**
     * 获取指定位置的对象
     * @param index 原下标
     * @return 对象
     */
    public T getItem(int index) {
        int bufferIndex = index & (size - 1);
        // volatile读
        return (T) U.getObjectVolatile(buffer, bufferIndex);
    }
}
