package com.ysx.java.data_structure.datastructure.self;


/**
 * @Description: 双向链表操作
 * @Author: ysx
 * @Date: 2020/7/20 15:41
 */
public class DoubleLinkedList {
    // 头结点用哨兵节点
    DoubleLinkNode sentryHead = new DoubleLinkNode(null,null);
    DoubleLinkNode tail = sentryHead;


    /**
     *  将节点添加到末尾
     */
    public void addNodeTotail(DoubleLinkNode doubleLinkNode){
        tail.next = doubleLinkNode;
        doubleLinkNode.prev = tail;
        tail = doubleLinkNode;
    }

    /**
     * 移除节点
     */
    public void removeNode(DoubleLinkNode removeNode) {
        DoubleLinkNode prev = removeNode.prev;
        DoubleLinkNode next = removeNode.next;
        prev.next = next;
        // 说明移除的是尾节点
        if(next==null){
            tail = prev;
        }else {
            next.prev = prev;
        }
        removeNode.prev = null;
        removeNode.next = null;
    }

    public DoubleLinkNode getSentryHead() {
        return sentryHead.next;
    }


    public void moveNodeToTail(DoubleLinkNode doubleLinkNode) {
        removeNode(doubleLinkNode);
        addNodeTotail(doubleLinkNode);
    }


    /**
     *  双向链表
     */
    static class DoubleLinkNode{
        Object key;
        Object value;
        DoubleLinkNode next;
        DoubleLinkNode prev;

        public DoubleLinkNode(Object key,Object value){
            this.key = key;
            this.value = value;
        }
    }
}
