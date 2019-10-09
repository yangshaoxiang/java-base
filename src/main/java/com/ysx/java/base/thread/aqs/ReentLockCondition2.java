package com.ysx.java.base.thread.aqs;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  队列 + Condition 实现生产消费模型 (参考这个生产消费 理解队列 put/take 的源码实现)
 */
public class ReentLockCondition2 {
	    private int queueSize = 10;
	    private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
	    private Lock lock = new ReentrantLock();
	    private Condition notFull = lock.newCondition();
	    private Condition notEmpty = lock.newCondition();
	     
	    public static void main(String[] args) throws InterruptedException  {
	    	ReentLockCondition2 test = new ReentLockCondition2();
	        Producer producer = test.new Producer();
	        Consumer consumer = test.new Consumer();	          
	        producer.start();
	        consumer.start();
	        Thread.sleep(1);
	        producer.interrupt();
	        consumer.interrupt();
	    }
	      
	    class Consumer extends Thread{	          
	        @Override
	        public void run() {
	            consume();
	        }
	        volatile boolean flag=true;  
	        private void consume() {
	            while(flag){
	                lock.lock();
	                try {
	                    while(queue.isEmpty()){
	                        try {
	                            System.out.println("队列空，等待数据");
	                            notEmpty.await();
	                        } catch (InterruptedException e) {	                          
	                            flag =false;
	                        }
	                    }
	                    queue.poll();                //每次移走队首元素
	                    notFull.signal();
	                    System.out.println("从队列取走一个元素，队列剩余"+queue.size()+"个元素");
	                } finally{
	                    lock.unlock();
	                }
	            }
	        }
	    }
	      
	    class Producer extends Thread{	          
	        @Override
	        public void run() {
	            produce();
	        }
	        volatile boolean flag=true;  
	        private void produce() {
	            while(flag){
	                lock.lock();
	                try {
	                    while(queue.size() == queueSize){
	                        try {
	                            System.out.println("队列满，等待有空余空间");
	                            notFull.await();
	                        } catch (InterruptedException e) {
	                            
	                            flag =false;
	                        }
	                    }
	                    queue.offer(1);        //每次插入一个元素
	                    notEmpty.signal();
	                    System.out.println("向队列取中插入一个元素，队列剩余空间："+(queueSize-queue.size()));
	                } finally{
	                    lock.unlock();
	                }
	            }
	        }
	    }
	}
