package com.pedro.event.interfaces;

/**
 * 消息工厂接口
 * T 消息类型
 */
public interface MessageFactory<T> {

    T createEmptyMessage();
}
