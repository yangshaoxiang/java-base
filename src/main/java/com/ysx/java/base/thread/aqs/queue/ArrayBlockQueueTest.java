package com.ysx.java.base.thread.aqs.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *  ArrayBlockingQueue 有界队列，队列本身使用数组实现
 *   关于队列实现上，包含一个 ReentrantLock 及其衍生出来的两个 Condition
 *   即在执行任何入队出队操作时都要使用 ReentrantLock 加锁，当使用 put/take操作 队列满/空时，使用 Condition.await（）阻塞当前线程(底层还是调用unsafe的park/unpark)
 *   每次入队和出队都执行对应的 Condition.signal() ps：Condition.signal()只是 单纯的将node节点从条件队列转移到CLH同步队列。其他什么都没做，真实的唤醒还需等到释放锁
 *
 *
 *  ----方法列表----
 *  add          增加一个元索                      如果队列已满，则抛出一个IIIegaISlabEepeplian异常 　　
 *  remove       移除并返回队列头部的元素          如果队列为空，则抛出一个NoSuchElementException异常 　
 *  　
 *  element      返回队列头部的元素                如果队列为空，则抛出一个NoSuchElementException异常 　
 *  　
 *  offer        添加一个元素并返回true            如果队列已满，则返回false 　
 *  　
 *  poll         移除并返问队列头部的元素          如果队列为空，则返回null 　　
 *  peek         返回队列头部的元素                如果队列为空，则返回null 　
 *  　
 *  put          添加一个元素                      如果队列满，则阻塞 　　
 *  take         移除并返回队列头部的元素          如果队列为空，则阻塞
 */
public class ArrayBlockQueueTest {
    public static void main1(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(1);
        arrayBlockingQueue.put("主线程放入的值");
        new Thread(() -> {
            try {
                arrayBlockingQueue.put("子线程放入的值"); // 断点
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"子线程").start();

        System.out.println("主线程----手动断点阻塞在这---"); // 断点

        arrayBlockingQueue.take();

    }

    // [[-1,-1,2],[-1,0,1]]
    public static void main(String[] args) {
        int[] aa = {-1,-100,3,99,78};
        reserve(aa);
        System.out.println(Arrays.toString(aa));
    }

    public static void reserve(int[] digits) {
        int length = digits.length;
        int mid = length / 2;
        for(int i = 0;i<=mid-1;i++){
            int qian = digits[i];
            digits[i] = digits[length-1-i];
            digits[length-1-i] = qian;
       }
    }

}
