package com.ysx.java.base.thread.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 1. Condition 由锁创建
 * 2. Condition 的 await 和 signal 必须在持有锁的线程中使用
 *
 *  注意:
 *  Condition的signal方法，即唤醒方法大体来说只是将代表当前线程的 Node 节点，从条件队列中移到 CLH 队列中(变更state字段从-2变为0，出条件队列，入CLH队列，CLH前驱节点转态变-1，表示该节点后面有节点待唤醒)，实际是没什么唤醒操作的
 *  真实的唤醒，还是调用Condition 的 signal 方法所在线程，释放 ReentLock 锁时，按照正常流程唤醒CLH队列阻塞节点
 *
 *  Condition的 await 方法 创建Node节点进入条件队列，释放当前线程持有的所有锁，最后调用 park 方法阻塞当前线程
 *
 *
 */
public class ReentLockCondition1 {
	
	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
 
	public static void main(String[] args) {
		ReentLockCondition1 test = new ReentLockCondition1();
	    Producer producer = test.new Producer();
	    Consumer consumer = test.new Consumer();
	    consumer.start(); 
	    producer.start();
	}
	
	 class Consumer extends Thread{
         
	        @Override
	        public void run() {
	            consume();
	        }
	          
	        private void consume() {
	           	             
	                try {
	                	lock.lock();
	                    System.out.println("我在等一个新信号"+currentThread().getName());
	                    condition.await();
	                    
	                } catch (InterruptedException e) {
						e.printStackTrace();
					} finally{
						System.out.println("拿到一个信号"+currentThread().getName());
	                    lock.unlock();
	                }
	            
	        }
	    }
	 
	 class Producer extends Thread{
         
	        @Override
	        public void run() {
	            produce();
	        }
	          
	        private void produce() {	             
	                try {
	                	   lock.lock();
	                       System.out.println("我拿到锁"+currentThread().getName());
	                        condition.signalAll();
	                    System.out.println("我发出了一个信号："+currentThread().getName());
	                } finally{
	                     lock.unlock();
	                }
	            }
	 }
	    
}
