package com.lbeen.spring.common.runable;

import java.util.concurrent.LinkedBlockingQueue;

@FunctionalInterface
public interface Producer<T> {
    void produce(LinkedBlockingQueue<T> queue) throws Exception;
}
