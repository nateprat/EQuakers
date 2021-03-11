package com.nateprat.university.mobileplatformdevelopment.core.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Thread pools for concurrency
 */

public final class ThreadPools {

    private static ThreadPools instance;
    private final ThreadPoolExecutor threadPoolExecutor;

    private static final int corePoolSize = 5;
    private static final int maxPoolSize = 10;
    private static final int keepAliveTime = 30;
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();

    private ThreadPools() {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, blockingQueue);
    }

    public static ThreadPools getInstance() {
        if (instance == null) {
            instance = new ThreadPools();
        }
        return instance;
    }

    public void submitTask(Runnable runnable) {
        threadPoolExecutor.submit(runnable);
    }

    public <T> Future<T> submitTask(Callable<T> callable) {
        return threadPoolExecutor.submit(callable);
    }

    public <T> T submitTask(Callable<T> callable, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        Future<T> future = threadPoolExecutor.submit(callable);
        return future.get(timeout, timeUnit);
    }

}
