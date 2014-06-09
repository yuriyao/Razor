/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.common.tools;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.footoo.common.exception.DirectoryCannotCreateException;

/**
 * 操作系统相关的工具
 * 
 * @author fengjing.yfj
 * @version $Id: SystemInfoTool.java, v 0.1 2014年2月13日 上午11:47:44 fengjing.yfj Exp $
 */
public abstract class SystemInfoTool {
    /**
     * 获取用户的目录
     * 
     * @return
     */
    public static final String getUserDir() {
        return System.getProperty("user.home");
    }

    /**
     * 获取完整的路径
     * 
     * @param dir 目录
     * @param path 路径
     * @return
     */
    public static final String getPath(String dir, String path) {
        return dir + File.separator + path;
    }

    /**
     * 获取相对于用户目录的绝对路径
     * 
     * @param path 相对于用户目录的路径
     * @return
     */
    public static final String getPathUnderUserDir(String path) {
        return getPath(getUserDir(), path);
    }

    /**
     * 获取完整路径
     * 
     * @param dir
     * @param paths
     * @return
     */
    public static final String getPath(String dir, String... paths) {
        for (String path : paths) {
            dir += File.separator + path;
        }
        return dir;
    }

    /**
     * 创建目录,如果不存在的话
     * 
     * @param path
     * @throws DirectoryCannotCreateException 
     */
    public static final void createDirIfNotExist(String path) throws DirectoryCannotCreateException {
        File dir = new File(path);
        //路径已经存在，并且不是目录
        if (dir.exists() && !dir.isDirectory()) {
            throw new DirectoryCannotCreateException("已经存在与文件夹名[" + path + "]相同的文件，无法创建文件夹");
        }
        //创建目录
        if (!dir.exists() && !dir.mkdirs()) {
            throw new DirectoryCannotCreateException("无法创建文件夹[" + path + "],请检查权限.");
        }
    }

    /**
     * 在用户目录下创建目录,如果不存在的话
     * 
     * @param path 相对于用户目录的路径
     * @throws DirectoryCannotCreateException
     */
    public static final void createDirUnderUserDirIfNotExist(String path)
                                                                         throws DirectoryCannotCreateException {
        createDirIfNotExist(getPathUnderUserDir(path));
    }

    /**
     * 获取本机所有的IPV4地址
     * 
     * @return
     */
    public static final List<String> getIps() {
        List<String> result = new ArrayList<>();
        try {
            //遍历所有的网卡
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ip = ips.nextElement();
                    String ipString = ip.getHostAddress();
                    if (isIpv4(ipString)) {
                        result.add(ipString);
                    }
                }
            }
        } catch (Exception e) {
            //忽略异常
            ;
        }

        return result;
    }

    /**
     * 获取正常的可以被外界访问到的IPV4地址
     * 
     * @return
     */
    public static final List<String> getNormalIps() {
        List<String> result = new ArrayList<>();
        try {
            //遍历所有的网卡
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ip = ips.nextElement();
                    String ipString = ip.getHostAddress();
                    //必须是IPV4和非回环地址
                    if (isIpv4(ipString) && !ip.isLoopbackAddress()) {
                        result.add(ipString);
                    }
                }
            }
        } catch (Exception e) {
            //忽略异常
            ;
        }

        return result;
    }

    /**
     * 判断IP地址是否是IPV4
     * 
     * @param ip
     * @return
     */
    public static final boolean isIpv4(String ip) {
        Pattern pattern = Pattern
            .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        return pattern.matcher(ip).matches();
    }

    /**
     * 获取机器的mac地址
     * 
     * @return
     */
    public static final List<byte[]> getMacs() {
        List<byte[]> macs = new ArrayList<>();
        try {
            //遍历所有的网卡
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    macs.add(mac);
                }
            }
        } catch (Exception e) {
            //忽略异常
            ;

        }
        return macs;
    }
}
