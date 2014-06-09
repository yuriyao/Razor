/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.config;

import java.io.InputStream;
import java.util.Properties;

import org.footoo.common.log.Logger;
import org.footoo.common.log.LoggerFactory;

/**
 * 全局配置文件
 * 
 * @author jeff
 * @version $Id: DoDoConfigure.java, v 0.1 2014年4月1日 下午5:15:11 jeff Exp $
 */
public abstract class DoDoConfigure {
    /** 日志 */
    private static final Logger            logger         = LoggerFactory
                                                              .getLogger(DoDoConfigure.class);
    /** 属性 */
    private static final Properties        properties     = new Properties();
    /** 配置文件名 */
    private static final String            CONFIGFILENAME = "dodo-config.ini";
    /** 配置信息 */
    private static final DoDoConfigureInfo configureInfo  = new DoDoConfigureInfo();

    static {
        try {
            //加载配置文件
            InputStream inputStream = DoDoConfigure.class.getClassLoader().getResourceAsStream(
                CONFIGFILENAME);
            if (inputStream == null) {
                logger.warn("无法加载系统配置文件[" + CONFIGFILENAME + "]");
                //直接终结
                System.exit(-1);
            } else {
                //解析配置文件
                properties.load(inputStream);
                //给配置信息赋属性
                configureInfo.assignAttrs(properties);
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.warn(e, "加载配置文件发生异常");
        }
    }

    /**
     * 获取配置信息
     */
    public static final DoDoConfigureInfo getConfigureInfo() {
        return configureInfo;
    }
}
