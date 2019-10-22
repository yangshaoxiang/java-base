package com.ysx.java.base.thread.aqs.queue;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  LinkedBlockingQueue 顾名思义，底层实现是链表(元素入队时要转化为链表节点 Node 对象 因此需要额外的消耗空间，对GC不友好)，其他实现上有两把锁，分别是put锁和take锁
 *  即在执行相应操作加相应的锁，即 可以在 put 时 take 增加并发度
 *
 *  默认是无界队列，但是可以指定容量变为有界队列
 *
 *  个人疑问？
 *  两把锁，当指定容量切换为有界队列时，容量满了，此时线程 释放 put 锁 且在 put锁的一个 条件队列中等待，当 take 消费完成时，如何唤醒 put锁条件队列的等待线程？
 *
 *  查看take源码 在队列容量消费之前是满的状态下，执行到最后会唤醒之前 可能因为容量满 而在执行 put 阻塞的线程
 *  这个唤醒流程其实就是 获取 put 锁，唤醒，释放 put 锁 整个流程
 *  因为 LinkedBlockingQueue 两把锁，一把take锁，一把put锁，take时，并未获取put锁，所以 take时想唤醒put的条件队列，就得走完上面说的整个唤醒流程
 */
public class LinkedBlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(1);
        blockingQueue.put("赛满容量");

        new Thread(() -> {
            try {
                blockingQueue.put("管你满不满，我要入队");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 出队
        blockingQueue.take(); // 断点

    }

    public static void main1(String[] args) {
        PriorityQueue<String> q = new PriorityQueue<String>();

    }
}
