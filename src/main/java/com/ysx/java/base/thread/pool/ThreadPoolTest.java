package com.ysx.java.base.thread.pool;

import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) {
        // 不带返回值线程池

        // 带返回值线程池

        // java 工具创建的线程池
    }


    /**
     *  执行流程
     * 如果当前池大小 poolSize 小于 corePoolSize ，则创建新线程执行任务
     *
     * 如果当前池大小 poolSize 大于 corePoolSize ，且等待队列未满，则进入等待队列
     *
     * 如果当前池大小 poolSize 大于 corePoolSize 且小于 maximumPoolSize ，且等待队列已满，则创建新线程 执行任务
     *     从这可以看出，如果是无界队列，即队列永远不会满，那么 maximumPoolSize 参数没有意义
     *
     * 如果当前池大小 poolSize 大于 corePoolSize 且大于 maximumPoolSize ，且等待队列已满，则调用拒绝策 略来处理该任务
     *
     * 线程池里的每个线程执行完任务后不会立刻退出，而是会去检查下等待队列里是否还有线程任务需要执行， 如果在 keepAliveTime 里等不到新的任务了，那么线程就会退出
     *
     */
    private static void testSimplePool(){
        // 核心线程数
        int corePoolSize = 6;
        // 最大线程数 - 核心线程已全部创建，且队列已满 额外创建的线程
        int maximumPoolSize = 15;
        // 线程完成任务后，多久没有任务销毁
        long keepAliveTime = 60;
        // 上面设置的时间单位 即小时，分钟，秒等
        TimeUnit unit = TimeUnit.SECONDS;
        // 任务队列 - 一个缓存队列，当核心线程都忙的时候，任务在队列中排队等待
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
        // 构造线程的工厂 - 设置线程名等，方便出现问题根据线程名排查
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("测试线程池-线程");
                return thread;
            }
        };
        // 拒绝策略 - 即 核心线程，队列，最大线程均已满，此时如何处理者溢出的任务
        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            if(!executor.isShutdown()){
                r.run();
                System.err.println("测试线程池线程数量不足，为保证任务完成，采用当前调度线程执行");
            }
        };
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, rejectedExecutionHandler);


    }


}
