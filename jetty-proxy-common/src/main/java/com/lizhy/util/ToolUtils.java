package com.lizhy.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * 工具类
 * Created by lizhiyang on 2017-04-26 14:56.
 */
public class ToolUtils {
    public static boolean isCollectionEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isMapEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 获取远程客户端访问IP地址
     * @param request
     * @return
     */
    public static String getRemoteIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip!=null && ip.length() > 15){ //"***.***.***.***".length() = 15
            if(ip.indexOf(",") > 0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }
}
