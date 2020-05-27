package com.lbeen.spring.common.runable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AssemblyLine<T> {
    private final LinkedBlockingQueue<T> queue;

    private final AssemblyLineWorkerFactory<T> workerFactory;

    private final int producerCount;
    private final CountDownLatch producerCdl;

    private final int consumerCount;
    private final CountDownLatch consumerCdl;

    public static <T> AssemblyLine<T> singleProducer(AssemblyLineWorkerFactory<T> workerFactory, int consumerCount) {
        return new AssemblyLine<>(workerFactory, 1, consumerCount);
    }

    private AssemblyLine(AssemblyLineWorkerFactory<T> workerFactory, int producerCount, int consumerCount) {
        this.queue = new LinkedBlockingQueue<>();

        this.workerFactory = workerFactory;

        this.producerCount = producerCount;
        this.producerCdl = new CountDownLatch(producerCount);

        this.consumerCount = consumerCount;
        this.consumerCdl = new CountDownLatch(consumerCount);
    }

    public void run() {
        for (int i = 0; i < producerCount; i++) {
            ThreadPool.execute(() -> {
                try {
                    workerFactory.producer().produce(queue);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    producerCdl.countDown();
                }
            });
        }

        for (int i = 0; i < consumerCount; i++) {
            ThreadPool.execute(() -> {
                try {
                    consume(workerFactory.consumer());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    consumerCdl.countDown();
                }
            });
        }
    }

    public boolean isFinish() {
        return producerCdl.getCount() == 0 && consumerCdl.getCount() == 0;
    }

    private void consume(Consumer<T> consumer) throws Exception {
        while (true) {
            T t = queue.poll(200, TimeUnit.MILLISECONDS);
            if (t != null) {
                consumer.consume(t);
                continue;
            }
            if (isProduceFinish()) {
                while ((t = queue.poll()) != null) {
                    consumer.consume(t);
                }
                consumer.finish();
                break;
            }
        }
    }

    private boolean isProduceFinish() {
        return producerCdl.getCount() == 0;
    }
}
