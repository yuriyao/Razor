/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.file;

import java.io.File;
import java.io.IOException;

/**
 * 可排序的MappedFile
 * 
 * @author jeff
 * @version $Id: SortableMappedFile.java, v 0.1 2014年3月4日 下午5:22:35 jeff Exp $
 */
public abstract class SortableMappedFile<T> extends MappedFile implements Comparable<T> {

    /**
     * @param file
     * @throws IOException
     */
    public SortableMappedFile(File file) throws IOException {
        super(file);
    }

    /**
     * @param fileName
     * @throws IOException
     */
    public SortableMappedFile(String fileName) throws IOException {
        super(fileName);
    }

    /**
     * @param fileName
     * @param size
     * @throws IOException
     */
    public SortableMappedFile(String fileName, long size) throws IOException {
        super(fileName, size);
    }

    /**
     * @param file
     * @param size
     * @throws IOException
     */
    public SortableMappedFile(File file, long size) throws IOException {
        super(file, size);
    }

}
