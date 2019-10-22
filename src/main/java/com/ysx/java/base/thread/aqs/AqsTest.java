package com.ysx.java.base.thread.aqs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  AQS juc 中自实现锁和并发工具实现的基石
 *
 * AQS 的主要属性
 *  CLH队列 一个双向链表，无法获取锁的线程创建一个链表节点加入到链表中，阻塞线程(AQS对CLH的一个变种，CLH本身会自旋，竞争激烈阻塞性能更好)
 *      头结点
 *      尾节点
 *  状态 加锁次数计数(可重入锁)
 *      state ：
 *            0  表示未获取锁的 CLH 节点的一般初始状态  常见的就是 CLH 队列中的尾节点
 *           -1  在 CLH 队列中除尾节点之外，其余节点的状态 表示的含义的是 该节点不是 CLH 最后一个节点，因此当该节点释放同步状态需要通知后继节点，使后继节点线程运行
 *           -2  表示节点在 条件队列中，当其他线程对Condition调用了signal()方法后，该节点会转移到 CLH 队列，状态变为 0 并将原来状态为0的 CLH 尾节点设为 -1
 *            1  表示节点 等待超时 或者节点对应线程被中断 此时设置节点状态为 1 取消等待
 *           -3  共享模式下，前继结点不仅会唤醒其后继结点，同时也可能会唤醒后继的后继结点
 *
 *       ps: 负值表示结点处于有效等待状态，而正值表示结点已被取消。所以源码中很多地方用>0、<0来判断结点的状态是否正常
 *
 *  当前独占线程
 *
 *  隐: 条件队列 即使用 Condition 时 调用 await 方法，会创建特殊的 node ，并加入到 条件队列中，调用 signal 方法时 将 node 转移到 CLH 队列
 *               和CLH队列的区别 主要是 1. 条件队列是单向的，非CLH那样双向
 *                                      2. CLH 插入节点时必须默认创建一个头结点，条件队列不用
 *                                      3. 队列的节点不同，条件队列没有使用CLH节点中的前驱，后驱节点，只用了独有的 nextWaiter 字段
 *
 * AQS 的阻塞和唤醒操作是 unsafe.park unpark  底层也是调操作系统，AQS线程唤醒，不会唤醒所有，唤醒CLH队列最近一个节点
 *
 * 在 ReentrantLock 的CLH队列实现中，会默认创建一个链表的头结点，作用是方便一些逻辑编程
 * 例如:快速判断节点是否在同步队列中，可以判断前驱节点是否为null，为null一定不再CLH队列中(在条件队列)
 *
 * 一些设计思想:
 * 参考 https://www.cnblogs.com/waterystone/p/4920797.html
 * 独占模式 tryAcquire-tryRelease 共享模式 tryAcquireShared-tryReleaseShared 等方法 并未设计成抽象方法 而是设计为普通方法，方法实现直接抛异常，明显意为开发者去实现
 * 那这里为何不设计为抽象方法，强制开发者去实现呢？因为强制实现，可能只需要使用AQS的独占模式，确不得一并不实现共享模式，反之亦然，所以这里未设计为抽象方法，而是抛异常，由开发者按需实现
 *
 */
public class AqsTest {
    // 测试 普通锁使用
    private static Integer counter = 0;
    private static ReentrantLock reentrantLock = new ReentrantLock(true);

    // 测试读写锁
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    // 测试 CountDownLatch 使用
    private static String currentThreadName = null;

    public static void main(String[] args) throws InterruptedException {
        //  -------- ReentrantLock 测试 ------
        //testReentLock();


        //  -------- Semaphore 测试 ------
        // testSemaphore();

        //  -------- CountDownLatch 测试 ------
        // 测试 CountDownLatch 的那个假并发猜想 猜想基本每次先执行的都是 子线程1
       /* int child1Thread = 0;
        int child2Thread = 0;
        int mainThread = 0;
        for(int i=0;i<100;i++){
            testCountDownLatch();
            if("子线程-1号".equals(currentThreadName)){
                child1Thread++;
            }
            if("子线程-2号".equals(currentThreadName)){
                child2Thread++;
            }
            if("main".equals(currentThreadName)){
                mainThread++;
            }
            currentThreadName = null;
        }
        System.out.println("子线程-1号:"+child1Thread+" 次先执行");
        System.out.println("子线程-2号:"+child2Thread+" 次先执行");
        System.out.println("main:"+mainThread+" 次先执行");*/

        // testCountDownLatch();

        //  --------  ReentrantReadWriteLock 读写锁测试 ------
       //  testReadAndWriteLock();

    }


    /**
     * CountDownLatch 小结:
     * 基于 AQS 的共享模式 非公平实现 同 Semaphore 初始化类似，即给 state 设置一个数值初始值
     *  调用 CountDownLatch 的 await 方法，若state 不为0，当前线程加入到 CLH 队列 并阻塞(unsafe.park())当前调用线程
     *  调用 CountDownLatch 的 countDown 方法，效果是 state - 1 当 state 减为 0 时，唤醒 CLH 中首节点下一节点的等待节点
     *        CLH 的其他等待节点由该等待节点的上一节点唤醒(unsafe.unpark()) ,只有首节点的下一节点由最后一个执行 countDown 方法的线程唤醒
     *
     *  因此对于我们使用 CountDownLatch 的一个场景:
     *   并发测试场景 即期望所有线程同时并发执行来模拟多线程并发，其实深究也并非是同时执行的(可以理解成另一种非公平的)
     *   因为在 CLH  队列中靠后的节点一般在执行时机上是稍靠后于靠前的节点(因为其本身的唤醒就需要上一节点唤醒)，当然线程的执行因为cpu时间片的调度当然是随机的只是前面节点执行概率远大于
     *   后面的节点
     *
     *   测试:
     *   保证CLH 队列顺序 子线程1 子线程2 main线程 后 测试100次state减为0时 线程1先执行94次 线程2线执行6次 main先执行0次 基本第一次阻塞唤醒后执行业务的线程是子线程1 基本上面猜想正确
     *
     *   断点位置 (jdk 1.8.0_172)
     *   AbstractQueuedSynchronizer 类 1342 行 state 减到 0 时开始释放节点
     *   AbstractQueuedSynchronizer 类 662 行  当前线程准备调用 unsafe.unpark() 方法 唤醒指定的线程
     */
    private static void testCountDownLatch() throws InterruptedException {
        int countDownLatchNum = 2;
        CountDownLatch countDownLatch = new CountDownLatch(countDownLatchNum);

        // 用于测试 CountDownLatch 基本使用的实现
        for (int i = 0;i<countDownLatchNum;i++){
            String threadName = "线程:"+i;
            new Thread(() -> {
                try {
                    doTask(threadName);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }
            },threadName).start();
        }
        // 用于测试 CLH 的其他等待节点由该等待节点的上一节点唤醒 而非 state 减到0时唤醒所有节点
        for(int i=1;i<3;i++){
            final int temi = i;
            new Thread(()->{
                System.out.println("子线程"+temi+"等待所有线程执行");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("子线程"+temi+"执行完毕");
                setCurrentThreadName(Thread.currentThread().getName());
            },"子线程-"+i+"号").start();
            Thread.sleep(10L); // 这个睡眠为了保证 CLH队列 顺序 是 子线程1 子线程2 main线程 用于测试唤醒后第一个可以执行业务的线程是子线程1
        }

        Thread.sleep(10L); // 这个睡眠为了保证 CLH队列 顺序 是 子线程1 子线程2 main线程 用于测试唤醒后第一个可以执行业务的线程是子线程1
        System.out.println("主线程等待所有任务完成");
        countDownLatch.await(); // TODO 断点
        System.out.println("所有任务执行完毕");
        setCurrentThreadName(Thread.currentThread().getName());
    }

     private static void  doTask(String threadName) throws InterruptedException {
         Random random = new Random();
         long randomInt = 0;
         // 防止生成负数
         do {
             randomInt = random.nextInt(1000);
         } while (randomInt <= 0);
         Thread.sleep(randomInt);
         System.out.println(threadName+"--耗时: "+randomInt+"ms 完成任务");
     }


    /**
     *  基于 AQS 的共享模式，初始设置 state 数量
     *  调用 Semaphore 的 acquire 方法，若state 大于 0，直接执行业务，state - 1，否则新线程加入到CLH队列，使用 unsafe.park() 阻塞线程
     *  调用 Semaphore 的 release 方法，state+1，唤醒CLH队列首节点下一节点，公平非公平和锁一样，即获取信号量时判断CLH队列是否有线程阻塞
     */
    private static void testSemaphore() {
        Semaphore semaphore = new Semaphore(2,true);
        for (int i = 0;i<10;i++){
            String threadName = "线程:"+i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        Thread.sleep(500);
                        System.out.println(threadName+" 执行成功");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        semaphore.release();
                    }
                }
            },threadName).start();
        }
    }


    private static synchronized void setCurrentThreadName(String currentThreadName){
        if(AqsTest.currentThreadName == null){
            AqsTest.currentThreadName = currentThreadName;
        }
    }


    /**
     *  读写锁
     *  1.读时其他线程可读 其余情况都互斥 (读时不可写，需读完)
     *  2.第一个线程读时当第二个线程线程要写，第二个线程写会阻塞，直到第一个线程读完成，此时如果有第三个线程要读，也会被阻塞，直到第二个线程写完成
     */
    private static void testReadAndWriteLock(){
          for (int i = 0; i<2; i++){
            // int i = 1;
            new Thread(()->{
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName+currentTime()+"读锁前准备执行");
                sleepCurrentThread(10);
                reentrantReadWriteLock.readLock().lock();
                System.out.println(threadName+currentTime()+"进入读锁-开始执行");
                sleepCurrentThread(3000);
                //System.out.println(threadName+currentTime()+"进入读锁-执行完毕");
                System.out.println(threadName+currentTime()+"预离开读锁");
                reentrantReadWriteLock.readLock().unlock();
            },"读锁线程-"+i).start();
       }

        reentrantReadWriteLock.writeLock().lock();
        System.out.println( Thread.currentThread().getName()+currentTime()+"进入写锁");
        sleepCurrentThread(3000);
        System.out.println( Thread.currentThread().getName()+currentTime()+"预离开写锁");
        reentrantReadWriteLock.writeLock().unlock();

    }


    /**
     *  AQS实现可重入原理是
     *                  独占模式下
     *                  加锁时，比较当前线程和锁持有线程是否是一个线程，同一个线程允许再次获取锁，有一个计数的属性，每加一次锁，次数加一
     * 					解锁时，将计数减一，减为0时，将独占线程置空，唤醒头结点指向的下一节点
     * 					         这里是一个链表的编程技巧，头结点只是一个无实意的节点对象，真正争锁的线程节点是头结点的后面节点
     *
     * AQS实现公平/非公平锁原理是
     *                  独占模式下 锁的竞争其实就是对状态的修改 (计数状态和当前独占线程)，公平锁就是在cas修改状态时，先判断队列中是否有节点，
     * 				    有节点就放弃竞争，加入队列，非公平锁就是直接竞争，可能会抢赢队列中的等待最久的节点，所以非公平
     */
    private static void testReentLock(){
        new Thread(()->{
            String threadName = Thread.currentThread().getName();
            modifyResources(threadName);
        },"Thread:老大").start();

        new Thread(()->{
            String threadName = Thread.currentThread().getName();
            modifyResources(threadName);
        },"Thread:老二").start();
    }

    /**
     * 需要保证多个线程使用的是同一个ReentrantLock对象
     */
    private static void modifyResources(String threadName){
        System.out.println("通知《管理员》线程:--->"+threadName+"准备打水");
        //默认创建的是独占锁，排它锁；同一时刻读或者写只允许一个线程获取锁
        reentrantLock.lock();
        System.out.println("线程:--->"+threadName+"第一次加锁");
        counter++;
        System.out.println("线程:"+threadName+"打第"+counter+"桶水");
        //重入该锁,我还有一件事情要做,没做完之前不能把锁资源让出去
        reentrantLock.lock();
        System.out.println("线程:--->"+threadName+"第二次加锁");
        counter++;
        System.out.println("线程:"+threadName+"打第"+counter+"桶水");
        reentrantLock.unlock();
        System.out.println("线程:"+threadName+"释放一个锁");
        reentrantLock.unlock();
        System.out.println("线程:"+threadName+"释放一个锁");
    }





    /**
     *  睡眠当前线程
     */
    private static void sleepCurrentThread(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取当前时间字符串
     */
    private static String currentTime(){
        return "-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"-";
    }



}
