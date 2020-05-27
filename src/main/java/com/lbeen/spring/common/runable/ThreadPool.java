package com.lbeen.spring.common.runable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }
}
