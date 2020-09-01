package com.ysx.java.base.thread.aqs.queue;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 优先级队列
 * https://mp.weixin.qq.com/s/-8kY2KImKO9jllntbyiwkA
 * PriorityQueue
 * 使用小顶堆(二叉堆)实现，是一个树形结构，小顶堆指的是根节点永远是最小节点
 * PriorityQueue 使用数组实现小顶堆，数组中存储顺序是树的元素层序遍历顺序
 *
 * PriorityQueue内使用的是小顶堆的结构，所以为了维持二叉堆的特点，每向堆内插入一个元素的时候，都要维持好父节点的值大与两个子节点的值，所以需要遵循这样的插入算法：
 *
 *   如果插入的节点值大于或等于它父节点的值，则插入成功。
 *
 *   如果插入的节点值小于它父节点的值，则需要把刚插入的这个节点和它父节点做交换。交换后继续和它上层的父节点做比较，重复这个规则。
 */
public class PriorityQueueTest {

    public static void main(String[] args) {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.add(5);    //第1步
        queue.add(7);    //第2步
        queue.add(3);    //第3步
        queue.add(4);    //第4步
        queue.add(2);    //第5步
        queue.add(9);    //第6步
        queue.add(1);    //第7步

        Integer i = queue.poll();   //获取并移除一个元素
        while (i != null) {
            System.out.println(i);
            i = queue.poll();
        }
    }
}
