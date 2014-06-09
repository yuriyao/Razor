/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.datastruct.graphic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 有向图
 * <ul>
 *  <li>有向图的表示使用邻接表的方式</li>
 *  <li>邻接表里存放有向边的起始节点列表</li>
 *  <li>有向图节点存放的数据必须不能相等，因为数据相等的节点被当成一个节点</li>
 * </ul>
 * 
 * @author jeff
 * @version $Id: DirectGraphic.java, v 0.1 2014年3月25日 下午3:30:11 jeff Exp $
 */
public class DirectGraphic<T> {
    /** 邻接表方式表示的图 */
    private Map<GraphicNode<T>, Set<GraphicNode<T>>> graphic = new HashMap<GraphicNode<T>, Set<GraphicNode<T>>>();

    /**
     * 默认的构造器
     */
    public DirectGraphic() {

    }

    /**
     * 拷贝构造函数
     * 
     * @param other
     */
    public DirectGraphic(DirectGraphic<T> other) {
        //进行深拷贝
        for (Map.Entry<GraphicNode<T>, Set<GraphicNode<T>>> entry : other.graphic.entrySet()) {
            Set<GraphicNode<T>> set = new HashSet<>(entry.getValue());
            this.graphic.put(entry.getKey(), set);
        }
    }

    /**
     * 获取图里面构成环的所有的节点的集合<br />
     * 使用的去边法
     * 
     * 
     * @return
     */
    public Set<GraphicNode<T>> findRings() {
        //拷贝一份
        DirectGraphic<T> copy = new DirectGraphic<>(this);

        boolean needAgain = true;
        while (needAgain) {
            needAgain = false;
            Iterator<GraphicNode<T>> iterator = copy.graphic.keySet().iterator();
            while (iterator.hasNext()) {
                GraphicNode<T> node = iterator.next();
                Set<GraphicNode<T>> set = copy.graphic.get(node);
                //入度为0
                if (set.isEmpty()) {
                    iterator.remove();
                    removeSetNode(node, copy.graphic);
                    needAgain = true;
                }
            }

        }

        return copy.graphic.keySet();
    }

    /**
     * 移除邻接表中的node节点
     * 
     * @param node
     * @param map
     */
    private void removeSetNode(GraphicNode<T> node, Map<GraphicNode<T>, Set<GraphicNode<T>>> map) {
        //移除大的节点
        //map.remove(node);
        //移除邻接表集合中的节点
        for (Map.Entry<GraphicNode<T>, Set<GraphicNode<T>>> entry : map.entrySet()) {
            entry.getValue().remove(node);
        }
    }

    /**
     * 插入有向边
     * 
     * @param from
     * @param to
     */
    public void insertEdge(T from, T to) {
        insertEdge(createOrGetNode(from), createOrGetNode(to));
    }

    /**
     * 插入有向边
     * 
     * @param from
     * @param to
     */
    public void insertEdge(GraphicNode<T> from, GraphicNode<T> to) {
        //获取起始节点的集合
        Set<GraphicNode<T>> nodes = graphic.get(to);
        if (nodes == null) {
            nodes = new HashSet<>();
            graphic.put(to, nodes);
        }
        nodes.add(from);
    }

    /**
     * 创建（如果不存在）或者获取（存在）对应的节点
     * 
     * @param value
     * @return
     */
    public GraphicNode<T> createOrGetNode(T value) {
        GraphicNode<T> ret = new GraphicNode<T>(value);
        if (!graphic.containsKey(ret)) {
            graphic.put(ret, new HashSet<GraphicNode<T>>());
        } else {
            ret = getNode(value);
        }

        assert (ret != null);
        return ret;
    }

    /**
     * 获取node
     * 
     * @param value
     * @return
     */
    private GraphicNode<T> getNode(T value) {
        for (GraphicNode<T> node : graphic.keySet()) {
            if (node.getValue().equals(value)) {
                return node;
            }
        }
        return null;
    }
}
