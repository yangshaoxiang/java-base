package com.ysx.java.data_structure.datastructure;
public class CircularQueue {
  // 数组：items，数组大小：n
  private String[] items;
  private int n = 0;
  // head 表示队头下标，tail 表示队尾下标
  private int head = 0;
  private int tail = 0;

  // 申请一个大小为 capacity 的数组
  public CircularQueue(int capacity) {
    items = new String[capacity];
    n = capacity;
  }

  // 入队
  public boolean enqueue(String item) {
	  System.out.println("当前tail:"+tail);
	  System.out.println("当前head:"+head);
    // 队列满了
    if ((tail + 1) % n == head) return false;
    items[tail] = item;
    tail = (tail + 1) % n;
    return true;
  }

  // 出队
  public String dequeue() {
    // 如果 head == tail 表示队列为空
    if (head == tail) return null;
    String ret = items[head];
    head = (head + 1) % n;
    return ret;
  }
  
  public static void main(String[] args) {
	CircularQueue queue = new CircularQueue(8);
	for(int i=0;i<15;i++){
		if(queue.enqueue(i+"")){
			System.out.println(i+"入队成功");
		}else{
			System.out.println(i+"入队失败");
			String string = queue.dequeue();
			System.out.println(string+"出队");
		}
	}
}
}
