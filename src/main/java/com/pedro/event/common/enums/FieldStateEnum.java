package com.pedro.event.common.enums;

/**
 * 容器栅格状态枚举
 */
public enum FieldStateEnum {

    /**
     * 空栅格，未被填充
     */
    NULL_FIELD,

    /**
     * 存在数据的栅格，已被写入
     */
    WRITTEN_FIELD,

    /**
     * 存在数据的栅格，已被读取
     */
    READ_FIELD

}
