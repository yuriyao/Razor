/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.datastruct.list;

import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 双向循环链表
 * <ul>
 *  <li>为了避免死锁，所有的封锁必须是按节点的顺序进行加锁</li>
 *  <li>有表头和表尾，并且固定</li>
 * </ul>
 * 
 * @author jeff
 * @version $Id: SimpleConcurrencyLinkedList.java, v 0.1 2014年5月8日 下午4:28:27 jeff Exp $
 */
public class SimpleConcurrencyLinkedList<T> implements Iterable<T> {
    /** 头指针 */
    private Entry head;
    /**尾指针 */
    private Entry tail;

    public SimpleConcurrencyLinkedList() {
        head = new Entry();
        tail = new Entry();
        head.setNext(tail);
        tail.setPrev(head);
    }

    /**
     * 链表是否为空
     * 
     * @return
     */
    public boolean isEmpty() {
        head.getRwLock().readLock().lock();
        boolean b = head.getNext() == tail;
        head.getRwLock().readLock().unlock();
        return b;
    }

    /**
     * 添加到表头
     * 
     * @param value
     */
    public void addToHead(T value) {
        Entry entry = new Entry(value);
        entry.setPrev(head);
        /** 锁住头 */
        head.getRwLock().writeLock().lock();
        Entry next = head.getNext();
        //锁住下一个节点
        next.getRwLock().writeLock().lock();

        entry.setNext(next);
        head.setNext(entry);
        next.setPrev(entry);

        //
        next.getRwLock().writeLock().unlock();

        head.getRwLock().writeLock().unlock();
    }

    /**
     * 这个只能在但线程调试环境下调用
     */
    public void dump() {
        System.out.print("From head:");
        Entry c = head.getNext();
        while (c != tail) {
            System.out.print(c.getValue() + " ");
            c = c.getNext();
        }
        System.out.println();

        System.out.print("From tail:");
        c = tail.getPrev();
        while (c != head) {
            System.out.print(c.getValue() + " ");
            c = c.getPrev();
        }
        System.out.println();
    }

    /**
     * 添加到链表的标尾
     * 
     * @param value
     */
    public void addToTail(T value) {
        Entry entry = new Entry(value);
        entry.setNext(tail);

        tail.getRwLock().readLock().lock();
        Entry prev = tail.getPrev();
        tail.getRwLock().readLock().unlock();

        //其实这是一个两阶段封锁协议的应用
        prev.getRwLock().writeLock().lock();
        tail.getRwLock().writeLock().lock();
        //设置节点信息
        entry.setPrev(prev);
        prev.setNext(entry);
        tail.setPrev(entry);
        tail.getRwLock().writeLock().unlock();
        prev.getRwLock().writeLock().unlock();
    }

    @Override
    public SCLLIterator iterator() {
        return new SCLLIterator();
    }

    /**
     * 迭代器
     * 
     * @author jeff
     * @version $Id: SimpleConcurrencyLinkedList.java, v 0.1 2014年5月8日 下午5:02:45 jeff Exp $
     */
    public class SCLLIterator implements Iterator<T> {
        /** 下一个节点 */
        private Entry next;
        /** 当前节点 */
        private Entry current;

        /**
         * 
         * @param head
         */
        public SCLLIterator() {
            head.getRwLock().readLock().lock();
            next = head.getNext();
            current = head;
            head.getRwLock().readLock().unlock();
        }

        @Override
        public boolean hasNext() {
            return next != tail;
        }

        @Override
        public T next() {
            current = next;
            if (current == tail) {
                throw new RuntimeException("迭代器越界");
            }
            current.getRwLock().readLock().lock();
            next = current.getNext();
            current.getRwLock().readLock().unlock();
            return current.getValue();
        }

        @Override
        public void remove() {
            if (current == head) {
                throw new RuntimeException("无法移除越界节点");
            }

            current.getRwLock().readLock().lock();
            Entry prev = current.getPrev();
            current.getRwLock().readLock().unlock();

            prev.getRwLock().writeLock().lock();
            next.getRwLock().writeLock().lock();
            //这个节点没有被并发线程移除掉
            if (prev.getNext() == current && next.getPrev() == current) {
                prev.setNext(next);
                next.setPrev(prev);
            }
            next.getRwLock().writeLock().unlock();
            prev.getRwLock().writeLock().unlock();
        }

        /**
         * 设置当前节点的数据
         * 
         * @param value
         */
        public void setValue(T value) {
            current.getRwLock().writeLock().lock();
            current.setValue(value);
            current.getRwLock().writeLock().unlock();
        }

    }

    /**
     * 每一个节点
     * 
     * @author jeff
     * @version $Id: SimpleConcurrencyLinkedList.java, v 0.1 2014年5月8日 下午4:32:53 jeff Exp $
     */
    private class Entry {
        /** 数据 */
        private T             value;
        /** 前一个节点 */
        private Entry         prev;
        /** 后一个节点 */
        private Entry         next;
        /** 读写锁 */
        private ReadWriteLock rwLock = new ReentrantReadWriteLock();

        public Entry() {

        }

        public Entry(T value) {
            this.value = value;
        }

        /**
         * Getter method for property <tt>value</tt>.
         * 
         * @return property value of value
         */
        public final T getValue() {
            return value;
        }

        /**
         * Setter method for property <tt>value</tt>.
         * 
         * @param value value to be assigned to property value
         */
        public final void setValue(T value) {
            this.value = value;
        }

        /**
         * Getter method for property <tt>prev</tt>.
         * 
         * @return property value of prev
         */
        public final Entry getPrev() {
            return prev;
        }

        /**
         * Setter method for property <tt>prev</tt>.
         * 
         * @param prev value to be assigned to property prev
         */
        public final void setPrev(Entry prev) {
            this.prev = prev;
        }

        /**
         * Getter method for property <tt>next</tt>.
         * 
         * @return property value of next
         */
        public final Entry getNext() {
            return next;
        }

        /**
         * Setter method for property <tt>next</tt>.
         * 
         * @param next value to be assigned to property next
         */
        public final void setNext(Entry next) {
            this.next = next;
        }

        /**
         * Getter method for property <tt>rwLock</tt>.
         * 
         * @return property value of rwLock
         */
        public final ReadWriteLock getRwLock() {
            return rwLock;
        }

    }

}
