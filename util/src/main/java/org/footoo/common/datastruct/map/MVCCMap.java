/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.datastruct.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.footoo.common.datastruct.list.SimpleConcurrencyLinkedList;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 使用多半本控制技术实现的用于高并发环境下的MAP
 * <ul>
 *  <li>版本使用的是时间戳，旧版本超过一定的时间会被删除掉</li>
 *  <li>对数据的访问和修改都需要锁住对应的桶</li>
 *  <li>put操作需要串行化</li>
 * </ul>
 * 
 * @author jeff
 * @version $Id: MVCCMap.java, v 0.1 2014年5月7日 上午10:34:27 jeff Exp $
 */
public class MVCCMap<K, V> implements Map<K, V> {
    /** 历史版本的默认过期时间（单位：毫秒） */
    public static final long                   DEFAULT_TIMEOUT_MS = 20 * 1000;
    /** 超时的时长 */
    private long                               timeOut            = DEFAULT_TIMEOUT_MS;
    /** 版本生成器 */
    private AtomicLong                         versionGenerator   = new AtomicLong();
    /** 所有的多版本的数据列表 */
    private SimpleConcurrencyLinkedList<Entry> entrys             = new SimpleConcurrencyLinkedList<>();
    /** 所有的Map的桶 */
    private Object[]                           buckets;
    /** 桶数组的容量 */
    private int                                capacity           = 8;
    /** 装载的数据 */
    private int                                amount             = 0;
    /** 日志 */
    private static final Logger                logger             = LoggerFactory
                                                                      .getLogger(MVCCMap.class);

    public MVCCMap() {
        buckets = new Object[capacity];
    }

    @Override
    public void clear() {
        synchronized (this) {
            capacity = 8;
            amount = 0;
            buckets = new Object[capacity];
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object key) {
        Bucket bucket = null;
        //获取操作版本
        long v = versionGenerator.incrementAndGet();

        bucket = (Bucket) buckets[calHash(key)];
        if (bucket == null) {
            return false;
        }
        //给桶加锁
        synchronized (bucket.getLock()) {
            Entry entry = bucket.getFirstEntry();
            while (entry != null) {
                //找到key
                if (key.equals(entry.getKey())) {
                    Value value = entry.getValue();
                    if (value.getCreateVersion() == Value.NULL_VERSION) {
                        logger.warn("数据的创建版本为空:key=[" + key + "]");
                    }
                    //比较版本
                    if (versionIsAccaptable(value.getCreateVersion(), value.getDeletedVersion(), v)) {
                        return true;
                    }
                }
            }
        }
        //
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        //生成操作版本
        long version = versionGenerator.incrementAndGet();
        //遍历所有的数据
        for (Entry entry : entrys) {
            Value v = entry.getValue();
            //获取正确的版本数据
            Value correntValue = v.findByVersion(version);
            //判断是否是相同的数据
            if (value.equals(correntValue.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V get(Object key) {
        //获取操作版本
        long version = versionGenerator.incrementAndGet();

        @SuppressWarnings("unchecked")
        Bucket bucket = (Bucket) buckets[calHash(key)];

        //锁住桶
        synchronized (bucket.getLock()) {
            Entry entry = findEntry(bucket, key);
            if (entry == null) {
                return null;
            }
            //获取对应的版本
            Value value = entry.getValue().findByVersion(version);
            //获取实际的数据
            if (value == null) {
                return null;
            }
            return value.getValue();
        }
    }

    @Override
    public boolean isEmpty() {
        return amount == 0;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    /**
     * put操作需要串行化
     * 
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(K key, V value) {
        //获取操作版本
        long version = versionGenerator.incrementAndGet();
        synchronized (this) {

        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return amount;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    /**
     * 计算key的桶位置
     * 
     * @param key
     * @return
     */
    private int calHash(Object key) {
        return key.hashCode() % capacity;
    }

    /**
     * 从桶中获取对应的k的entry
     * 
     * @param bucket
     * @param k
     * @return
     */
    private Entry findEntry(Bucket bucket, Object k) {
        Entry entry = bucket.getFirstEntry();
        while (entry != null) {
            if (k.equals(entry.getKey())) {
                return entry;
            }
            entry = entry.getNext();
        }
        return null;
    }

    /**
     * 是否需要扩容,装载率在2/3时会进行扩容
     * 
     * @param amount
     * @param capacity
     * @return
     */
    private boolean needResize(int amount, int capacity) {
        if (amount * 3 > capacity * 2) {
            return true;
        }
        return false;
    }

    /**
     * 桶
     * 
     * @author jeff
     * @version $Id: MVCCMap.java, v 0.1 2014年5月8日 下午11:08:45 jeff Exp $
     */
    private class Bucket {
        /** 第一个节点 */
        private Entry  firstEntry;
        /** 桶的锁 */
        private Object lock = new Object();

        /**
         * Getter method for property <tt>firstEntry</tt>.
         * 
         * @return property value of firstEntry
         */
        public final Entry getFirstEntry() {
            return firstEntry;
        }

        /**
         * Setter method for property <tt>firstEntry</tt>.
         * 
         * @param firstEntry value to be assigned to property firstEntry
         */
        public final void setFirstEntry(Entry firstEntry) {
            this.firstEntry = firstEntry;
        }

        /**
         * Getter method for property <tt>lock</tt>.
         * 
         * @return property value of lock
         */
        public final Object getLock() {
            return lock;
        }

    }

    /**
     * 每一个节点
     * 
     * @author jeff
     * @version $Id: MVCCMap.java, v 0.1 2014年5月7日 上午10:59:03 jeff Exp $
     */
    @SuppressWarnings("unused")
    private class Entry {
        /** 键值 */
        private final K key;
        /** 键值对应的锁 */
        private Object  lock = new Object();
        /** 对应的实际的数据 */
        private Value   value;
        /** 下一个节点 */
        private Entry   next;

        public Entry(K key) {
            this.key = key;
        }

        /**
         * Getter method for property <tt>key</tt>.
         * 
         * @return property value of key
         */
        public final K getKey() {
            return key;
        }

        /**
         * Getter method for property <tt>lock</tt>.
         * 
         * @return property value of lock
         */
        public final Object getLock() {
            return lock;
        }

        /**
         * Getter method for property <tt>value</tt>.
         * 
         * @return property value of value
         */
        public final Value getValue() {
            return value;
        }

        /**
         * Setter method for property <tt>value</tt>.
         * 
         * @param value value to be assigned to property value
         */
        public final void setValue(Value value) {
            this.value = value;
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

        public int hashCode() {
            return key.hashCode();
        }
    }

    /**
     * 测试数据的版本是否可以被当前的版本接受
     * 
     * @param createVersion
     * @param deletedVersion
     * @param v
     * @return
     */
    private boolean versionIsAccaptable(long createVersion, long deletedVersion, long v) {
        if (createVersion <= v && (deletedVersion == Value.NULL_VERSION || deletedVersion > v)) {
            return true;
        }
        return false;
    }

    /**
     * 值
     * 
     * @author jeff
     * @version $Id: MVCCMap.java, v 0.1 2014年5月7日 下午6:20:48 jeff Exp $
     */
    @SuppressWarnings("unused")
    private class Value {
        /** 实际的数据 */
        private final V          value;
        /** 需要真正进行删除的时间戳 */
        private long             deletedTime;
        /** 创建时的版本 */
        private long             createVersion  = NULL_VERSION;
        /** 删除时的版本 */
        private long             deletedVersion = NULL_VERSION;
        /** 前一个版本 */
        private Value            prev;
        /** 空 */
        public static final long NULL_VERSION   = -1;

        public Value(V value) {
            this.value = value;
        }

        public Value(V value, long createdVersion) {
            this.value = value;
            this.createVersion = createdVersion;
        }

        /**
         * 根据版本获取能够感知的版本
         * 
         * @param v
         * @return
         */
        public Value findByVersion(long v) {
            if (versionIsAccaptable(createVersion, deletedVersion, v)) {
                return this;
            }
            //从前一个版本的数据找
            Value p = prev;
            if (p != null) {
                return p.findByVersion(v);
            }
            return null;
        }

        /**
         * Getter method for property <tt>deletedTime</tt>.
         * 
         * @return property value of deletedTime
         */
        public final long getDeletedTime() {
            return deletedTime;
        }

        /**
         * Setter method for property <tt>deletedTime</tt>.
         * 
         * @param deletedTime value to be assigned to property deletedTime
         */
        public final void setDeletedTime(long deletedTime) {
            this.deletedTime = deletedTime;
        }

        /**
         * Getter method for property <tt>createVersion</tt>.
         * 
         * @return property value of createVersion
         */
        public final long getCreateVersion() {
            return createVersion;
        }

        /**
         * Setter method for property <tt>createVersion</tt>.
         * 
         * @param createVersion value to be assigned to property createVersion
         */
        public final void setCreateVersion(long createVersion) {
            this.createVersion = createVersion;
        }

        /**
         * Getter method for property <tt>deletedVersion</tt>.
         * 
         * @return property value of deletedVersion
         */
        public final long getDeletedVersion() {
            return deletedVersion;
        }

        /**
         * Setter method for property <tt>deletedVersion</tt>.
         * 
         * @param deletedVersion value to be assigned to property deletedVersion
         */
        public final void setDeletedVersion(long deletedVersion) {
            this.deletedVersion = deletedVersion;
        }

        /**
         * Getter method for property <tt>prev</tt>.
         * 
         * @return property value of prev
         */
        public final Value getPrev() {
            return prev;
        }

        /**
         * Setter method for property <tt>prev</tt>.
         * 
         * @param prev value to be assigned to property prev
         */
        public final void setPrev(Value prev) {
            this.prev = prev;
        }

        /**
         * Getter method for property <tt>value</tt>.
         * 
         * @return property value of value
         */
        public final V getValue() {
            return value;
        }
    }

}
