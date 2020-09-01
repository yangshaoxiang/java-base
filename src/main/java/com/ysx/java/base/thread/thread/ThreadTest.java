package com.ysx.java.base.thread.thread;

import sun.misc.Unsafe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class ThreadTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // ThreadExceptionCatch();
        // testCall();
       // testPark();
        con();
    }

    private static void con(){
        CountDownLatch countDownLatch = new CountDownLatch(10);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("唤醒");
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0;i<100;i++){
            System.out.println(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

    }

    /**
     *  测试 park 方法 -- 可以先unpark，后面unpark的线程park时，直接过去，不会阻塞
     */
    private static void testPark() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程--进入-预备睡2s"+"--"+currentTime());
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LockSupport.park();
                System.out.println("子线程--完毕"+"--"+currentTime());
            }
        },"子线程");
        t1.start();
        //Thread.sleep(2000L);
        System.out.println("主线程解锁子线程");
        LockSupport.unpark(t1);
    }


    /**
     *  测试返回值线程
     *
     */
    private static void testCall() throws InterruptedException, ExecutionException {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("进入callable");
                Thread.sleep(2000);
                return 1;
            }
        };
        FutureTask<Integer> task = new FutureTask<>(callable);
        Thread thread = new Thread(task);
        thread.start();
        Integer result = task.get();
        System.out.println("取到线程中结果:"+result);

        System.out.println("主线程走完");
    }

    /**
     * 线程异常捕获，直接在主线程中是捕获不到的
     *   使用异常处理器 - 可在线程内部处理线程
     */
    private static void ThreadExceptionCatch(){
        int tmp = 1;
        // 构造一个可以内部发生异常的线程
        Runnable runnable = () -> {
            System.out.println("进入子线程");
            int a = 5/0;
            System.out.println("离开子线程");
        };
        Thread thread = new Thread(runnable,"外捕获异常");
        // 验证 外部是否可以捕获线程内部异常 -- 结论: 不可以
        try{
            thread.start();
        }catch (Exception e){
            System.out.println("主线程捕获到异常:"+thread.getName());
        }
        System.out.println("主线程测试一执行完毕");

        //
        Thread th = new Thread(runnable, "异常处理器");
        th.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("线程:"+Thread.currentThread().getName()+" 捕获到异常");
            }
        });
        th.start();
    }

    /**
     *  获取当前时间字符串
     */
    private static String currentTime(){
        return "-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }


}
