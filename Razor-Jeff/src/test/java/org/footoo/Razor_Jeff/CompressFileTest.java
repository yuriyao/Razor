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

/**
 * 
 * @author jeff
 * @version $Id: CompressFileTest.java, v 0.1 2014年6月10日 下午10:15:54 jeff Exp $
 */
public class CompressFileTest {

    /**
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        String fileName = "test/Why.java";
        String outputFileName = "test/why.je";
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
        byte[] result = compress.compress(byteBuffer.toByteArray());
        //写入文件
        OutputStream outputStream = new FileOutputStream(outputFileName);
        outputStream.write(result);
        //释放资源
        inputStream.close();
        outputStream.close();

        System.out.println("Compress OK");
    }

}
