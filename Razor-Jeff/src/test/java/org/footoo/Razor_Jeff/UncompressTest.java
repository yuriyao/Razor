/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.footoo.common.buffer.ByteBuffer;
import org.footoo.common.compress.Compress;
import org.footoo.common.compress.DoDoCompress;
import org.footoo.common.compress.InvalidCompressedDataException;

/**
 * 
 * @author jeff
 * @version $Id: uncompressTest.java, v 0.1 2014年6月10日 下午10:16:21 jeff Exp $
 */
public class UncompressTest {

    /**
     * 
     * @param args
     * @throws IOException 
     * @throws InvalidCompressedDataException 
     */
    public static void main(String[] args) throws IOException, InvalidCompressedDataException {
        String fileName = "test/why.je";
        String outputFileName = "test/why3.java";
        //获取输入文件的数据
        InputStream inputStream = new FileInputStream(fileName);
        byte[] buffer = new byte[1024];
        ByteBuffer byteBuffer = new ByteBuffer();
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.append(buffer, 0, len);
        }
        //压缩工具
        Compress compress = new DoDoCompress();
        byte[] result = compress.uncompress(byteBuffer.toByteArray());
        //写入文件
        OutputStream outputStream = new FileOutputStream(outputFileName);
        outputStream.write(result);
        //释放资源
        inputStream.close();
        outputStream.close();
        System.out.println("UnCompress OK");
    }

}
