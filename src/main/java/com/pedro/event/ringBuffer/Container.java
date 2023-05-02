package com.pedro.event.ringBuffer;

import com.pedro.event.interfaces.MessageFactory;
import com.pedro.event.util.UnsafeUtil;
import sun.misc.Unsafe;

import java.util.ArrayList;

/**
 * 环形缓冲区的实际对象容器
 * 使用volatile来处理是为了保证读写的立即可见性
 */
abstract class ContainerField<T> {

    protected long p1, p2, p3, p4, p5, p6, p7;

    /**
     * 元素数组
     */
    protected Object[] buffer;

    protected long p8, p9, p10, p11, p12, p13, p14, p15;

    /**
     * Container大小
     */
    protected int size;

    /**
     * UNSAFE
     */
    protected static final Unsafe U = UnsafeUtil.getUnsafe();

    /**
     * UNSAFE操作数组信息
     */
    long base = U.arrayBaseOffset(Object[].class);
    int shift = 31 - Integer.numberOfLeadingZeros(U.arrayIndexScale(Object[].class));

    protected long p16, p17, p18, p19, p20, p21, p22, p23;

}

public class Container<T> extends ContainerField<T> {

    /**
     * 构造器
     *
     * @param size           buffer大小
     * @param messageFactory 消息工厂
     */
    public Container(int size, MessageFactory<T> messageFactory) {
        // 1.属性填充
        this.buffer = new Object[size];
        this.size = size;
        // 2.预填充对象
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = messageFactory.createEmptyMessage();
        }
    }

    /**
     * 获取指定位置的对象 volatile
     *
     * @param index 原下标
     * @return 对象
     */
    public T getItem(long index) {
        // 1.获取真实下标
        int bufferIndex = (int) (index & (size - 1));
        // 2.volatile读
        return (T) U.getObjectVolatile(buffer, ((long) bufferIndex << shift) + base);
    }

    /**
     * 在指定位置放置对象 volatile
     *
     * @param index 原下标
     * @param item  对象
     */
    public void putItem(long index, T item) {
        // 1.获取真实下标
        int bufferIndex = (int) (index & (size - 1));
        // 2.volatile写
        U.putObjectVolatile(buffer, ((long) bufferIndex << shift) + base, item);
    }
}
