package com.ysx.java.base.thread.cas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 *  cas 对数组的操作
 *  注意点 构造 AtomicIntegerArray 类传入数组值 之后会生成一个克隆的数组 使用 AtomicIntegerArray 类原子操作，操作的都是克隆后的数组，对原始数组无影响
 */
public class AtomicIntegerArrayTest {
    private static int[] intArray = {1,2,3};
    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(intArray);

    public static void main(String[] args) {
        // 第一个参数代表数组的下标
        int newValue = 100;
        int oldValue = atomicIntegerArray.getAndSet(0, newValue);
        // 原子操作 新值:100 旧值:1
        System.out.println("原子操作 新值:"+newValue+" 旧值:"+oldValue);
        int atomicIntegerArrayValue = atomicIntegerArray.get(0);
        int arrayValue = intArray[0];
        // 可测试得出  使用 AtomicIntegerArray 类原子操作，操作的都是克隆后的数组，对原始数组无影响
        // AtomicIntegerArray 值:100  原始数组值:1
        System.out.println("AtomicIntegerArray 值:"+atomicIntegerArrayValue+"  原始数组值:"+arrayValue);
        AtomicInteger atomicInteger = new AtomicInteger(10);
        atomicInteger.compareAndSet(1,10);
    }
}
