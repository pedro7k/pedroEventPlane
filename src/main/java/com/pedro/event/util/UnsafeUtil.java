package com.pedro.event.util;

import com.pedro.event.PedroEventPlane;
import com.pedro.event.common.exception.PedroEventPlaneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import static com.pedro.event.common.enums.PedroEventPlaneExceptionEnum.PEDRO_EVENT_PLANE_ERROR;

/**
 * Unsafe工具类
 */
public class UnsafeUtil {

    private static final Logger logger = LoggerFactory.getLogger(UnsafeUtil.class);

    // UNSAFE对象提供
    private static final Unsafe unsafe;

    static {
        try {
            final PrivilegedExceptionAction<Unsafe> action = new PrivilegedExceptionAction<Unsafe>() {
                public Unsafe run() throws Exception {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                }
            };

            unsafe = AccessController.doPrivileged(action);
        } catch (Throwable e) {
            logger.error("[pedroEventPlane]获取Unsafe实例失败");
            throw new PedroEventPlaneException(PEDRO_EVENT_PLANE_ERROR);
        }
    }

    /**
     * 获得 Unsafe 实例
     *
     * @return Unsafe实例
     */
    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
