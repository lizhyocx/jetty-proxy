package com.lizhy.constants;

import java.io.File;

/**
 * 工程相关常量
 * Created by lizhiyang on 2017-04-26 13:56.
 */
public class ProjectConstants {
    public static final String JETTY_CONF_FILE = ".."+ File.separator+"conf"+File.separator+"jetty-proxy.conf";
    public static final String JETTY_HTTP_PORT = "jetty.server.http.port";
    public static final String JETTY_MIN_THREAD = "jetty.server.threapool.minthread";
    public static final String JETTY_MAX_THREAD = "jetty.server.threadpool.maxthread";

    public static final String JETTY_DISPATCHER_URL = "j_dis_url";
}
