/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author jeff
 * @version $Id: FileOperateTest.java, v 0.1 2014年6月10日 上午11:58:21 jeff Exp $
 */
public class FileOperateTest {

    /**
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("test.txt", "rw");
        file.seek(10);
        file.write("a".getBytes());
        file.close();

    }

}
