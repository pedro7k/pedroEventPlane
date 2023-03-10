package com.pedro.event.ringBuffer;

import junit.framework.TestCase;

public class PointerTest extends TestCase {

    public void testGet() {

        Pointer pointer = new Pointer();
        pointer.set(1);
        System.out.println(pointer.get());
        pointer.setVolatile(2);
        System.out.println(pointer.get());
        pointer.compareAndSet(2,3);
        System.out.println(pointer.get());
    }

    public void testSet() {
    }

    public void testSetVolatile() {
    }

    public void testCompareAndSet() {
    }
}