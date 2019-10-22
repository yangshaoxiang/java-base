package com.ysx.java.base.thread.pool;

import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main1(String[] args) {
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
       for(int i=0;i<10;i++){
           int tmp = i;
           poolExecutor.execute(() -> System.out.println(tmp+"线程池小线程。。。"+Thread.currentThread().getName()));
       }



    }

    public static void main(String[] args) {
        testSimplePool();
    }

   /* private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }
    runStateOf：获取运行状态；
    workerCountOf：获取活动线程数；
    ctlOf：获取运行状态和活动线程数的值*/



 /*   public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
*//*
 * clt记录着runState和workerCount
 *//*
    int c = ctl.get();
*//*
 * workerCountOf方法取出低29位的值，表示当前活动的线程数；
 * 如果当前活动线程数小于corePoolSize，则新建一个线程放入线程池中；
 * 并把任务添加到该线程中。
 *//*
    if (workerCountOf(c) < corePoolSize) {
*//*
 * addWorker中的第二个参数表示限制添加线程的数量是根据corePoolSize
来判断还是maximumPoolSize来判断；
 * 如果为true，根据corePoolSize来判断；
 * 如果为false，则根据maximumPoolSize来判断
 *//*
        if (addWorker(command, true))
            return;
*//*
 * 如果添加失败，则重新获取ctl值
 *//*
        c = ctl.get();
    }
*//*
 * 如果当前线程池是运行状态并且任务添加到队列成功
 *//*
    if (isRunning(c) && workQueue.offer(command)) {
// 重新获取ctl值
        int recheck = ctl.get();
 // 再次判断线程池的运行状态，如果不是运行状态，由于之前已经把command
            添加到workQueue中了，
// 这时需要移除该command
// 执行过后通过handler使用拒绝策略对该任务进行处理，整个方法返回
        if (! isRunning(recheck) && remove(command))
            reject(command);
*//*
 * 获取线程池中的有效线程数，如果数量是0，则执行addWorker方法
 * 这里传入的参数表示：
 * 1. 第一个参数为null，表示在线程池中创建一个线程，但不去启动；
 * 2. 第二个参数为false，将线程池的有限线程数量的上限设置为
maximumPoolSize，添加线程时根据maximumPoolSize来判断；
 * 如果判断workerCount大于0，则直接返回，在workQueue中新增的
command会在将来的某个时刻被执行。
 *//*
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
*//*
 * 如果执行到这里，有两种情况：
 * 1. 线程池已经不是RUNNING状态；
 * 2. 线程池是RUNNING状态，但workerCount >= corePoolSize并且
workQueue已满。
 * 这时，再次调用addWorker方法，但第二个参数传入为false，将线程池的有限
线程数量的上限设置为maximumPoolSize；
 * 如果失败则拒绝该任务
 *//*
    else if (!addWorker(command, false))
        reject(command);
    }*/


  /*  // 线程池 execute 方法
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        int c = ctl.get();
        // 先使用核心线程
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        // 核心线程不足 使用阻塞队列缓存
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            // 重新检查后发现线程池已不处于 running状态 ，移除线程
            if (! isRunning(recheck) && remove(command))
                reject(command);
            // 检查 若当前工作的线程为0，新建线程去工作（可能的原因是所有线程都允许超时销毁，此时所有线程都销毁了）
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        // 阻塞队列不够存，新建非核心线程 -- 非核心线程创建失败
        else if (!addWorker(command, false))
            reject(command);
    }*/



   /* private boolean addWorker(Runnable firstTask, boolean core) {
    retry:
    for (;;) {
        int c = ctl.get();
    // 获取运行状态
        int rs = runStateOf(c);
    *//*
     * 这个if判断
     * 如果rs >= SHUTDOWN，则表示此时不再接收新任务；
     * 接着判断以下3个条件，只要有1个不满足，则返回false：
     * 1. rs == SHUTDOWN，这时表示关闭状态，不再接受新提交的任务，但却
可以继续处理阻塞队列中已保存的任务
     * 2. firsTask为空
     * 3. 阻塞队列不为空
     *
     * 首先考虑rs == SHUTDOWN的情况
     * 这种情况下不会接受新提交的任务，所以在firstTask不为空的时候会返回
false；
     * 然后，如果firstTask为空，并且workQueue也为空，则返回false，
     * 因为队列中已经没有任务了，不需要再添加线程了
     *//*
        // Check if queue empty only if necessary.
        if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN &&
                        firstTask == null &&
                        ! workQueue.isEmpty()))
            return false;
        for (;;) {
            // 获取线程数
            int wc = workerCountOf(c);
            // 如果wc超过CAPACITY，也就是ctl的低29位的最大值（二进制是
                29个1），返回false；
            // 这里的core是addWorker方法的第二个参数，如果为true表示
                根据corePoolSize来比较，
            // 如果为false则根据maximumPoolSize来比较。
            //
            if (wc >= CAPACITY ||
                    wc >= (core ? corePoolSize : maximumPoolSize))
                return false;
            // 尝试增加workerCount，如果成功，则跳出第一个for循环
            if (compareAndIncrementWorkerCount(c))
                break retry;
            // 如果增加workerCount失败，则重新获取ctl的值
            c = ctl.get();  // Re­read ctl
            // 如果当前的运行状态不等于rs，说明状态已被改变，返回第一个
                for循环继续执行
            if (runStateOf(c) != rs)
                continue retry;
            // else CAS failed due to workerCount change; retry
                inner loop
        }
    }
    boolean workerStarted = false;
    boolean workerAdded = false;
    Worker w = null;
    try {
     // 根据firstTask来创建Worker对象
        w = new Worker(firstTask);
     // 每一个Worker对象都会创建一个线程
        final Thread t = w.thread;
        if (t != null) {
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                int rs = runStateOf(ctl.get());
                // rs < SHUTDOWN表示是RUNNING状态；
                // 如果rs是RUNNING状态或者rs是SHUTDOWN状态并且
                    firstTask为null，向线程池中添加线程。
                // 因为在SHUTDOWN时不会在添加新的任务，但还是会执行
                    workQueue中的任务
                if (rs < SHUTDOWN ||
                        (rs == SHUTDOWN && firstTask == null)) {
                    if (t.isAlive()) // precheck that t is
                        startable
                        throw new IllegalThreadStateException();
                    // workers是一个HashSet
                    workers.add(w);
                    int s = workers.size();
                    // largestPoolSize记录着线程池中出现过的最大线程数量
                    if (s > largestPoolSize)
                        largestPoolSize = s;
                    workerAdded = true;
                }
            } finally {
                mainLock.unlock();
            }
            if (workerAdded) {
                // 启动线程
                t.start();
                workerStarted = true;
            }
        }
    } finally {
        if (! workerStarted)
            addWorkerFailed(w);
    }
    return workerStarted;
    }*/

}
