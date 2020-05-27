package com.lbeen.spring.common.runable;

public interface AssemblyLineWorkerFactory<T> {
    Producer<T> producer();

    Consumer<T> consumer();
}
