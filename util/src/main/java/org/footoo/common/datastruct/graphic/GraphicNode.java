/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.datastruct.graphic;

/**
 * 图的节点
 * 
 * @author jeff
 * @version $Id: GraphicNode.java, v 0.1 2014年3月25日 下午3:15:20 jeff Exp $
 */
public class GraphicNode<T> {
    /** 节点保存的数据 */
    private T value;

    public GraphicNode(T value) {
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
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return value.hashCode();
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GraphicNode)) {
            return false;
        }
        return this.value.equals(((GraphicNode<?>) other).value);
    }

}
