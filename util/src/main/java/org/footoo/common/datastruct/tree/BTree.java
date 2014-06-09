/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.datastruct.tree;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * B树
 * 
 * @author jeff
 * @version $Id: BTree.java, v 0.1 2014年4月1日 上午9:30:03 jeff Exp $
 */
public class BTree<T> {
    /**
     * BTree的节点
     * 
     * @author jeff
     * @version $Id: BTree.java, v 0.1 2014年4月1日 上午9:34:42 jeff Exp $
     */
    private class Node<T> {
        /** 关键字数量 */
        private int       number;
        /** 关键字数组 */
        private T[]       keywords;
        /** 指向孩子节点 */
        private Node<T>[] childs;
        /** 指向父节点 */
        private Node<T>   parent;

        @SuppressWarnings("unchecked")
        public Node(Class<T> type, int number) {
            this.number = 0;
            keywords = (T[]) Array.newInstance(type, number);
            childs = new Node[number + 1];
            parent = null;
        }
    }

    /** 根节点 */
    private Node<T>        root;
    /** B树的路数 */
    private final int      m;
    /** 比较器 */
    private Comparator<T>  comparator;

    /** 类型 */
    private final Class<T> type;

    /**
     * 创建一个M路的B树
     * 
     * @param m
     */
    public BTree(Class<T> type, int m) {
        this.m = m;
        this.type = type;
        root = new Node<T>(type, m);
    }

    /**
     * 插入一个关键字
     * 
     * @param value
     */
    public void insert(T value) {
        int position = findPosition(root, value);
        Node<T> node = root;
        int parent = 0;

        while (node.childs[position] != null) {
            node = node.childs[position];
            parent = position;
            position = findPosition(node, value);
        }

    }

    /**
     * 
     * 
     * @param node
     * @param value
     * @return
     */
    private int findPosition(Node<T> node, T value) {
        int i = 0;
        for (; i < node.number; i++) {
            if (comparator.compare(node.keywords[i], value) <= 0) {
                break;
            }
        }
        return i;
    }

    /**
     * 插入到Node中
     * 
     * @param node 必须没有孩子节点
     * @param value
     */
    private void insert(Node<T> node, T value) {
        int i = node.number;
        for (; i > 0; i--) {
            if (comparator.compare(node.keywords[i - 1], value) > 0) {
                node.keywords[i] = node.keywords[i - 1];
            } else {
                break;
            }
        }
        node.keywords[i] = value;
        node.number++;
    }

    private void insert(Node<T> node, T value, Node<T> left, Node<T> right) {
        int i = node.number;
        for (; i > 0; i--) {
            if (comparator.compare(node.keywords[i - 1], value) > 0) {
                node.keywords[i] = node.keywords[i - 1];
            } else {
                break;
            }
        }
        node.keywords[i] = value;
        node.number++;
    }

    private Node<T> split(Node<T> node, boolean left) {
        //保持根节点不变
        if (node.parent == null) {
            Node<T> child1 = new Node<>(type, m);
            Node<T> child2 = new Node<>(type, m);
            int leftNumber = node.number / 2;
            int rightNumber = node.number / 2;
            if (left) {
                leftNumber = ceil(node.number);
            }
            System.arraycopy(node.keywords, 0, child1.keywords, 0, node.number / 2);
            System.arraycopy(node.childs, 0, child1.childs, 0, node.number / 2 + 1);

        }

        return node;
    }

    private int ceil(int number) {
        int half = number / 2;
        return half * 2 == number ? half : half + 1;
    }
}
