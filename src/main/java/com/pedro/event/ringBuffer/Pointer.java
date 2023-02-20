package com.pedro.event.ringBuffer;

import com.pedro.event.common.enums.PedroEventPlaneExceptionEnum;
import com.pedro.event.common.exception.PedroEventPlaneException;
import com.pedro.event.util.UnsafeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;


abstract class PointerField {

    protected long p1, p2, p3, p4, p5, p6, p7;

    /**
     * 指针值
     */
    protected volatile long index;

    protected long p8, p9, p10, p11, p12, p13, p14, p15;

}

/**
 * 指针
 */
public class Pointer extends PointerField {

    private static final Logger logger = LoggerFactory.getLogger(UnsafeUtil.class);

    /**
     * UNSAFE工具
     */
    private static final Unsafe U = UnsafeUtil.getUnsafe();
    private static final long VALUE_OFFSET;

    static {
        try {
            VALUE_OFFSET = U.objectFieldOffset(PointerField.class.getDeclaredField("index"));
        } catch (Throwable e) {
            logger.error("[pedroEventPlane]获取Pointer值地址失败");
            throw new PedroEventPlaneException(PedroEventPlaneExceptionEnum.PEDRO_EVENT_PLANE_ERROR);
        }
    }

    /**
     * 无参构造器
     */
    public Pointer() {
    }

    /**
     * 有参构造器-初始位置
     *
     * @param initialIndex 初始位置
     */
    public Pointer(long initialIndex) {
        U.putOrderedLong(this, VALUE_OFFSET, initialIndex);
    }

    /**
     * volatile读
     *
     * @return 当前值
     */
    public long get() {
        return index;
    }

    /**
     * 无冲突写
     *
     * @param newValue 新值
     */
    public void set(final long newValue) {
        U.putOrderedLong(this, VALUE_OFFSET, newValue);
    }

    /**
     * volatile写
     *
     * @param newValue 新值
     */
    public void setVolatile(final long newValue) {
        U.putLongVolatile(this, VALUE_OFFSET, newValue);
    }

    /**
     * cas操作
     *
     * @param expectedValue 期望值
     * @param newValue      新值
     * @return 是否成功
     */
    public boolean compareAndSet(final long expectedValue, final long newValue) {
        return U.compareAndSwapLong(this, VALUE_OFFSET, expectedValue, newValue);
    }

}
