package com.pedro.event.ringBuffer.impl;

import com.pedro.event.ringBuffer.Container;
import junit.framework.TestCase;

public class SingleRingBufferTest  extends TestCase {

    public void testPublish(){
        SingleRingBuffer<String> buffer = new SingleRingBuffer<>(8, () -> "test");
        buffer.publish("testMessage");
        System.out.println(buffer.getContainer().getItem(0));
        System.out.println(buffer.getContainer().getItem(1));
        buffer.publish("testMessage2");
        System.out.println(buffer.getContainer().getItem(0));
        System.out.println(buffer.getContainer().getItem(1));
        System.out.println(buffer.getContainer().getItem(2));
    }
}
