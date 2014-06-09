/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.file;

import java.io.File;
import java.io.IOException;

/**
 * 根据名字进行排序的文件
 * 
 * @author jeff
 * @version $Id: SortableByNameMappedFile.java, v 0.1 2014年3月4日 下午5:28:44 jeff Exp $
 */
public class SortableByNameMappedFile extends SortableMappedFile<SortableByNameMappedFile> {
    /** 文件名 */
    private String name;

    /**
     * @param file
     * @throws IOException
     */
    public SortableByNameMappedFile(File file) throws IOException {
        super(file);
        setName(file.getName());

    }

    /**
     * @param fileName
     * @throws IOException
     */
    public SortableByNameMappedFile(String fileName) throws IOException {
        super(fileName);
        setName(fileName);
    }

    /**
     * @param fileName
     * @param size
     * @throws IOException
     */
    public SortableByNameMappedFile(String fileName, long size) throws IOException {
        super(fileName, size);
        setName(fileName);
    }

    /**
     * @param file
     * @param size
     * @throws IOException
     */
    public SortableByNameMappedFile(File file, long size) throws IOException {
        super(file, size);
        setName(file.getName());
    }

    /**
     * 设置文件名
     * 
     * @param fullName
     */
    private void setName(String fullName) {
        String ss[] = fullName.split(File.pathSeparator);
        name = ss[ss.length - 1];
    }

    /** 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SortableByNameMappedFile other) {

        return this.name.compareTo(other.name);
    }
}
