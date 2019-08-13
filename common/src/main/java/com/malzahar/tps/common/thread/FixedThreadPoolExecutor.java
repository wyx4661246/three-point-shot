package com.malzahar.tps.common.thread;


import java.util.concurrent.*;

public class FixedThreadPoolExecutor extends ThreadPoolExecutor {
    public FixedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime,
                                         final TimeUnit unit,
                                         final BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public FixedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime,
                                         final TimeUnit unit,
                                         final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public FixedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime,
                                         final TimeUnit unit,
                                         final BlockingQueue<Runnable> workQueue, final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public FixedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime,
                                         final TimeUnit unit,
                                         final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory,
                                         final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        return new FutureTaskExt<T>(runnable, value);
    }

}
