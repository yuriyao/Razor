/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.datastruct.list;

import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简单的并发链表
 * <ul>
 * <li>
 *  没表头
 * </li>
 *  <li>
 * 这个链表可以实现多线程读，但同时最好只能有一个线程写，
 * 如果存在多线程写，可能会出现更新丢失的现象
 * </li>
 * <li>
 *  所有的添加操作只能是在链表尾和表头
 * </li>
 * <li>
 *  所有迭代器是相互独立的，可以并行操作，线程安全的
 * </li>
 * </ul>
 * @author jeff
 * @version $Id: SimpleConcurrencyList.java, v 0.1 2014年5月7日 下午7:33:24 jeff Exp $
 */
public class SimpleConcurrencyList<T> implements Iterable<T> {
    /** 表头 */
    private Entry         head   = null;
    /** 标尾 */
    private Entry         tail   = null;
    /** 读写锁 */
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 头插数据
     * 
     * @param value
     */
    public void addHead(T value) {
        Entry entry = new Entry(value);
        rwLock.writeLock().lock();
        entry.setNext(head);
        head = entry;
        if (tail == null) {
            tail = entry;
        }
        rwLock.writeLock().unlock();
    }

    /**
     * 尾插数据
     * 
     * @param value
     */
    public void addTail(T value) {
        Entry entry = new Entry(value);
        rwLock.writeLock().lock();
        //空链表
        if (tail == null) {
            tail = head = entry;
            return;
        }
        //
        tail.setNext(entry);
        tail = entry;
        rwLock.writeLock().unlock();
    }

    @Override
    public Iterator<T> iterator() {
        return new SCLIterator(head);
    }

    private class SCLIterator implements Iterator<T> {
        /** 前一个节点 */
        private Entry former;
        /** 下一个节点 */
        private Entry next;
        /** 当前节点 */
        private Entry current;

        public SCLIterator(Entry head) {
            current = null;
            next = head;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (next == null) {
                throw new RuntimeException("迭代器越界");
            }
            former = current;
            current = next;
            next = current.getNext();
            return current.getValue();
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new RuntimeException("当前没有可移除的数据");
            }

            if (former != null) {
                former.setNext(next);
            }
            //特殊处理
            rwLock.writeLock().lock();
            if (head == current) {
                head = next;
            }
            if (tail == current) {
                tail = former;
            }
            rwLock.writeLock().unlock();
        }

    }

    /**
     * 
     * 
     * @author jeff
     * @version $Id: SimpleConcurrencyList.java, v 0.1 2014年5月7日 下午7:51:56 jeff Exp $
     */
    @SuppressWarnings("unused")
    private class Entry {
        /** 保存的数据 */
        private T             value;
        /** 下一个节点 */
        private Entry         next;
        /** 读写锁 */
        private ReadWriteLock rwLock = new ReentrantReadWriteLock();

        public Entry(T value) {
            this.value = value;
        }

        public void setValue(T value) {
            rwLock.writeLock().lock();
            this.value = value;
            rwLock.writeLock().unlock();
        }

        public T getValue() {
            rwLock.readLock().lock();
            T value = this.value;
            rwLock.readLock().unlock();
            return value;
        }

        public void setNext(Entry next) {
            rwLock.writeLock().lock();
            this.next = next;
            rwLock.writeLock().unlock();
        }

        public Entry getNext() {
            rwLock.readLock().lock();
            Entry entry = this.next;
            rwLock.readLock().unlock();
            return entry;
        }
    }

}
