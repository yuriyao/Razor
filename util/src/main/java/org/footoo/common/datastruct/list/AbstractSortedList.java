/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.datastruct.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author jeff
 * @version $Id: SortedList.java, v 0.1 2014年4月2日 下午11:51:14 jeff Exp $
 * @param <E>
 */
public abstract class AbstractSortedList<E extends Comparable<E>> implements List<E> {
    /** 数据的数量 */
    private int size = 0;

    /**
     * 不支持这样的操作 
     * 
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (collection == null) {
            return false;
        }
        //将元素添加到列表当中
        for (E element : collection) {
            add(element);
            size++;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        clearSelf();
        size = 0;
    }

    /**
     * 对应的实现去具体clear
     */
    protected abstract void clearSelf();

    @Override
    public boolean contains(Object e) {
        //遍历所有的元素
        for (E element : this) {
            if (element.equals(e)) {
                return true;
            }
        }
        return false;
    }

    /*@Override
    public boolean containsAll(Collection<？> collection) {
        //比较数量
        if (collection.size() > this.size()) {
            return false;
        }
        //正常情况下，只要对collection进行一下排序，比较就非常简单了，
        //但是排序会导致colleaction的内部结构变化
        //或者创建一个新的有序列表，但是比较耗空间，所以选择n^2的比较
        for (Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }*/
    @Override
    public E get(int index) {
        //越界
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return null;
    }

    @Override
    public int indexOf(Object arg0) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int lastIndexOf(Object arg0) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int arg0) {
        return null;
    }

    @Override
    public boolean remove(Object arg0) {
        return false;
    }

    @Override
    public E remove(int arg0) {
        return null;
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        return false;
    }

    @Override
    public E set(int arg0, E arg1) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public List<E> subList(int arg0, int arg1) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        return null;
    }

}
