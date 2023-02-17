package com.pedro.event.common.exception;


import com.pedro.event.common.enums.PedroEventPlaneExceptionEnum;

/**
 * 服务端统一异常
 */
public final class PedroEventPlaneException extends RuntimeException {

    /**
     * 错误码
     */
    private final String status;

    public PedroEventPlaneException(PedroEventPlaneExceptionEnum pedroEventPlaneExceptionEnum) {
        // 使用父类的 message 字段
        super(pedroEventPlaneExceptionEnum.getMsg());
        // 设置错误码
        this.status = pedroEventPlaneExceptionEnum.getStatus();
    }

    public String getStatus() {
        return status;
    }
}
