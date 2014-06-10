/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.file;

import java.io.IOException;
import java.io.InputStream;

import org.footoo.Razor_Jeff.exception.RazorException;
import org.footoo.Razor_Jeff.exception.RazorStandError;
import org.footoo.common.buffer.ByteBuffer;
import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 文件流的操作
 * 
 * @author jeff
 * @version $Id: FileStreamOperator.java, v 0.1 2014年6月10日 下午3:46:50 jeff Exp $
 */
public abstract class FileStreamOperator {
    /** 每次读取的缓冲区的大小 */
    public static final int     BUFFER_LEN = 1024;
    /** 日志 */
    private static final Logger logger     = LoggerFactory.getLogger(FileStreamOperator.class);

    /**
     * 读取n个字节的数据,除非是遇到了文件的结尾
     * 
     * @param in
     * @param n
     * @return
     * @throws RazorException
     */
    public static byte[] readBytes(InputStream in, int n) throws RazorException {
        ByteBuffer byteBuffer = new ByteBuffer();
        byte[] buffer = new byte[BUFFER_LEN];
        while (n > 0) {
            int readLen = BUFFER_LEN;
            readLen = readLen > n ? n : readLen;

            try {
                readLen = in.read(buffer, 0, readLen);
            } catch (IOException e) {
                logger.error(e, "读取文件发生异常");
                throw new RazorException(RazorStandError.READ_FILE_FAIL, e.getMessage());
            }
            //读完
            if (readLen < 0) {
                break;
            }
            byteBuffer.append(buffer, 0, readLen);
            n -= readLen;
        }
        return byteBuffer.toByteArray();
    }
}
