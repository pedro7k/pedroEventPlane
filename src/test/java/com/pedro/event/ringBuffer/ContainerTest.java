package com.pedro.event.ringBuffer;

import junit.framework.TestCase;

public class ContainerTest extends TestCase {

    public void testContainer() {
        Container<String> container = new Container<>(8, () -> "test");
        System.out.println("create success");
        System.out.println(container.getItem(5));

        container.putItem(2,"good");
        System.out.println(container.getItem(2));
        System.out.println(container.getItem(3));
    }
}
