package com.lbeen.spring.common.runable;

public interface Consumer<T> {
    void consume(T t);

    void finish();
}
