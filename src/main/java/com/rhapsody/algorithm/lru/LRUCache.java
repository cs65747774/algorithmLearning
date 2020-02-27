package com.rhapsody.algorithm.lru;

import java.util.Hashtable;

public class LRUCache {
    // 使用hashMap存储cache
    private Hashtable<String, DLinkedNode> cache = new Hashtable<String, DLinkedNode>();
    private int count;
    private int capacity;
    private DLinkedNode head, tail;

    public LRUCache(int capacity) {
        this.count = 0;
        this.capacity = capacity;
        head = new DLinkedNode();
        head.preNode = null;
        tail = new DLinkedNode();
        tail.postNode = null;
        tail.preNode = head;
        head.postNode = tail;
    }

    public int get(String key) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        // 如果节点已经有，移动到链表头
        this.moveToHead(node);
        return node.value;
    }

    /**
     * 添加node
     * @param node
     */
    private void addNode(DLinkedNode node) {
        node.preNode = head;
        node.postNode = head.postNode;
        head.postNode.preNode = node;
        head.postNode = node;
    }

    private void removeNode(DLinkedNode node) {
        DLinkedNode preNode = node.preNode;
        DLinkedNode postNode = node.postNode;

        preNode.postNode = postNode;
        postNode.preNode = preNode;
    }

    /**
     * 设置node
     * @param key
     * @param value
     */
    public void set(String key, int value) {
        DLinkedNode node = cache.get(key);

        if(node == null){

            DLinkedNode newNode = new DLinkedNode();
            newNode.key = key;
            newNode.value = value;

            this.cache.put(key, newNode);
            this.addNode(newNode);

            ++count;

            if(count > capacity){
                // pop the tail
                DLinkedNode tail = this.popTail();
                this.cache.remove(tail.key);
                --count;
            }
        }else{
            // update the value.
            node.value = value;
            this.moveToHead(node);
        }
    }

    /**
     * 移动节点到链表头
     * @param node
     */
    private void moveToHead(DLinkedNode node) {
        this.removeNode(node);
        this.addNode(node);
    }

    private DLinkedNode popTail(){
        DLinkedNode res = tail.preNode;
        this.removeNode(res);
        return res;
    }
}
