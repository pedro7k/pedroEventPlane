package com.pedro.event.common.enums;

/**
 * 异常类型枚举
 */
public enum PedroEventPlaneExceptionEnum {

    /**
     * 错误状态和信息枚举
     */
    PEDRO_EVENT_PLANE_ERROR("0", "[pedroEventPlane]pedroEventPlane出现异常");

    /**
     * 错误码
     */
    private String status;
    /**
     * 错误提示
     */
    private String msg;

    PedroEventPlaneExceptionEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
