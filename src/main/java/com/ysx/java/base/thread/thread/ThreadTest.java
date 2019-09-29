package com.ysx.java.base.thread.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //ThreadExceptionCatch();

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("进入callable");
                Thread.sleep(1000);
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
}
