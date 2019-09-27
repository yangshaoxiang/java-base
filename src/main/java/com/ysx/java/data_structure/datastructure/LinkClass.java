package com.ysx.java.data_structure.datastructure;

/**
 * 自实现链表类，该类线程不安全
 */
public class LinkClass {

	/**
	 * 头结点
	 */
	private SingleNode head;
	
	/**
	 * 是否环链表(只有添加方法实现了环链表)
	 */
	private boolean cycle = false;
	
	

	public LinkClass() {}
	
	public LinkClass(boolean cycle) {
		this.cycle = cycle;
	}

	/**
	 * 向链表中顺序追加数据
	 * 
	 * @param node
	 *            追加的数据
	 */
	public SingleNode addNode(Object data) {
		SingleNode node = new SingleNode(data, null);
		if (head == null) {
			head = node;
			return node;
		}
		SingleNode tmp = head;
		while (tmp.next != null) {
			//如果是环，下一节点是头结点，说明已到尾节点
			if(cycle&&tmp.next==head){
				break;
			}
			tmp = tmp.next;
		}
		tmp.next = node;
		//是否环链表
		if(cycle){
			node.next = head;
		}
		return node;
	}

	/**
	 * 移除链表中指定数据
	 * 
	 * @param data
	 *            要移除的数据
	 */
	public void delNode(Object data) {
		if (head == null) {
			return;
		}
		// 如果头结点就满足条件，删除头结点
		while (head != null && head.data.equals(data)) {
			head = head.next;
		}
		// 头结点不满足条件或满足已被删除，剩下的遍历链表，删除满足条件的节点
		SingleNode tmp = head;
		while (tmp != null) {
			// 不需比较当前节点，当前节点值一定不是指定值，
			if (tmp.next != null && data.equals(tmp.next.data)) {
				// 既然当前节点的下一节点需删除，则删除后当前节点的下一节点是下下节点
				tmp.next = tmp.next.next;
			} else {
				// 说明当前节点的下一节点不满足删除条件，下次循环，下一节点为当前节点(保证当前节点是不满足删除条件的)
				tmp = tmp.next;
			}
		}
	}

	/**
	 * 单链表反转
	 */
	public void reverse() {
		// SingleNode tmp = head;
		SingleNode next = null;
		SingleNode pre = null;
		if (head == null) {
			return;
		}

		while (head.next != null) {
			// 当前下一节点
			next = head.next;
			// 当前头下一节点引用应变为前一节点
			head.next = pre;
			// 当前前一节点应变为头结点
			pre = head;
			// 当前头结点应变为下一节点
			head = next;
		}
		head.next = pre;
	}


	//检测任意节点所在链表是否是环链表
	//使用快慢指针
	public boolean isCycle(SingleNode anyNode){
		if(anyNode==null||anyNode.next==null){
			return false;
		}
		SingleNode fast = anyNode;//.next;
		
		SingleNode slow = anyNode;
		int i = 0;
		while(fast!=null&&fast.next!=null){
			slow =  slow.next;
			fast = fast.next.next;
			Object slowData = slow.data;
			Object fastData = fast.data;
			//System.out.println(++i+"次确定链表为环链表:slow:"+slowData+"  fast:"+fastData);
			if(slowData.equals(fastData)){
				//如果有环，求出环节点
				
				
				return true;
			}
		}
		
		return false;
		
	}
	

	/**
	 * 打印链表中所有数据
	 */
	public void printAllNode() {
		SingleNode tmp = head;
		System.out.print("[");
		while (tmp != null) {
			// 说明最后一次循环，无需打印逗号
			if (tmp.next == null) {
				System.out.print(tmp.data);
			} else {
				System.out.print(tmp.data + " , ");
			}
			tmp = tmp.next;
		}
		System.out.print("]");
		System.out.println();
	}

	/**
	 * 代表一个单链表节点
	 */
	public static class SingleNode {
		/**
		 * 链表数据部分
		 */
		private Object data;

		/**
		 * 当前节点指向下一节点的指针(引用)
		 */
		private SingleNode next;

		public SingleNode(Object data, SingleNode next) {
			super();
			this.data = data;
			this.next = next;
		}

		public static void main(String[] args) {
		/*	LinkClass linkClass = new LinkClass();
			for (int i = 0; i < 10; i++) {
				linkClass.addNode(i);
			}
			linkClass.printAllNode();

			linkClass.delNode(0);
			linkClass.delNode(1);
			linkClass.delNode(9);
			linkClass.printAllNode();

			linkClass.reverse();
			linkClass.printAllNode();*/
			
			LinkClass linkClass = new LinkClass(true);
			SingleNode node = null;
			for (int i = 1; i < 3; i++) {
				if(i==1){
					 node = linkClass.addNode(i);
				}else{
					linkClass.addNode(i);
				}
			}
			SingleNode singleNode = linkClass.addNode(3);
			
			boolean cycle2 = linkClass.isCycle(node);
			System.out.println("是否环:"+cycle2);
			
			
			
		}

	}
}
