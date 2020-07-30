package com.ysx.java.data_structure.datastructure.self;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: ysx
 * @Date: 2020/7/20 16:22
 */
public class LruCache {
    private int capacity;
    private Map<Object,DoubleLinkedList.DoubleLinkNode> map = new HashMap<Object,DoubleLinkedList.DoubleLinkNode>();
    private DoubleLinkedList doubleLinkedList = new DoubleLinkedList();

    public LruCache(int capacity){
        this.capacity = capacity;
    }

    public Object get(Object key) {
        if(map.containsKey(key)){
            DoubleLinkedList.DoubleLinkNode doubleLinkNode = map.get(key);
            // 将 节点移动到链表末尾
            doubleLinkedList.moveNodeToTail(doubleLinkNode);
            return doubleLinkNode.value;
        }
        return null;
    }

    public void put(Object key, Object value) {
        if(map.containsKey(key)){
            DoubleLinkedList.DoubleLinkNode doubleLinkNode = map.get(key);
            doubleLinkNode.value = value;
            doubleLinkedList.moveNodeToTail(doubleLinkNode);
        }else{
            if(capacity==map.size()){
                // 移除当前链表头结点数据
                DoubleLinkedList.DoubleLinkNode headNode = doubleLinkedList.getSentryHead();
                map.remove(key);
                doubleLinkedList.removeNode(headNode);
            }
            // 新增节点
            DoubleLinkedList.DoubleLinkNode doubleLinkNode = new DoubleLinkedList.DoubleLinkNode(key, value);
            doubleLinkedList.addNodeTotail(doubleLinkNode);
            map.put(key,doubleLinkNode);
        }


    }
}
