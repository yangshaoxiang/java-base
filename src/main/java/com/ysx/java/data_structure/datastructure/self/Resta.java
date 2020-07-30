package com.ysx.java.data_structure.datastructure.self;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: lc 完整的 lrucache 实现
 * @Author: ysx
 * @Date: 2020/7/21 9:51
 */
public class Resta {

    private int capacity;
    private Map<Object,DoubleLinkedList.DoubleLinkNode> map = new HashMap<Object,DoubleLinkedList.DoubleLinkNode>();
    private DoubleLinkedList doubleLinkedList = new DoubleLinkedList();

    public Resta(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        if(map.containsKey(key)){
            DoubleLinkedList.DoubleLinkNode doubleLinkNode = map.get(key);
            // 将 节点移动到链表末尾
            doubleLinkedList.moveNodeToTail(doubleLinkNode);
            return (int)doubleLinkNode.value;
        }
        return -1;
    }

    public void put(int key, int value) {
        if(map.containsKey(key)){
            DoubleLinkedList.DoubleLinkNode doubleLinkNode = map.get(key);
            doubleLinkNode.value = value;
            doubleLinkedList.moveNodeToTail(doubleLinkNode);
        }else{
            if(capacity==map.size()){
                // 移除当前链表头结点数据
                DoubleLinkedList.DoubleLinkNode headNode = doubleLinkedList.getSentryHead();
                map.remove(headNode.key);
                doubleLinkedList.removeNode(headNode);
            }
            // 新增节点
            DoubleLinkedList.DoubleLinkNode doubleLinkNode = new DoubleLinkedList.DoubleLinkNode(key, value);
            doubleLinkedList.addNodeTotail(doubleLinkNode);
            map.put(key,doubleLinkNode);
        }
    }


    static class DoubleLinkedList {
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


    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        // [[1],[2,1],[2],[3,2],[2],[3]]
        Resta resta = new Resta(1);
        resta.put(2,1);
        resta.get(2);
        resta.put(3,2);
        resta.get(2);
        resta.get(3);

    }
}