/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.exception;

/**
 * 系统的标准错误码
 * 
 * @author jeff
 * @version $Id: RazorStandError.java, v 0.1 2014年6月10日 上午10:01:48 jeff Exp $
 */
public interface RazorStandError {
    /** 空的压缩文件列表 */
    public static final RazorStandError EMPTY_FILES               = new RazorStandErrors(1,
                                                                      "空的压缩文件列表");
    /** 读取文件发生错误 */
    public static final RazorStandError READ_FILE_FAIL            = new RazorStandErrors(2,
                                                                      "读取文件发生错误");
    /** 创建输出文件失败 */
    public static final RazorStandError CREATE_FILE_FAILED        = new RazorStandErrors(3,
                                                                      "创建输出文件失败");
    /** 无法写入数据 */
    public static final RazorStandError CANNOT_WRITE_FILE         = new RazorStandErrors(4,
                                                                      "无法写入数据");
    /** 文件不存在 */
    public static final RazorStandError FILE_NOT_EXISTS           = new RazorStandErrors(5, "文件不存在");
    /** 无法定位文件的偏移 */
    public static final RazorStandError CANNOT_LOCATE_FILE_OFFSET = new RazorStandErrors(6,
                                                                      "无法定位文件的偏移");
    /** 操作文件发生异常 */
    public static final RazorStandError OPERATE_FILE_FAILED       = new RazorStandErrors(7,
                                                                      "操作文件发生异常");
    /** 不合法的压缩文件 */
    public static final RazorStandError INVALID_COMPRESSED_FILE   = new RazorStandErrors(8,
                                                                      "不合法的压缩文件");
    /** 无法创建目录 */
    public static final RazorStandError CANNOT_CREATE_DIR         = new RazorStandErrors(9,
                                                                      "无法创建目录");

    /**
     * 获取错误码
     * 
     * @return
     */
    public int getCode();

    /**
     * 获取错误信息
     * 
     * @return
     */
    public String getInfo();
}
