package com.lizhy.constants;

import java.io.File;

/**
 * 工程相关常量
 * Created by lizhiyang on 2017-04-26 13:56.
 */
public class ProjectConstants {
    public static final String JETTY_CONF_FILE = ".."+ File.separator + "conf" + File.separator + "jetty-proxy.conf";
    public static final String JETTY_LOGBACK_FILE = ".." + File.separator + "conf" + File.separator + "logback.xml";
    public static final String JETTY_HTTP_PORT = "jetty.server.http.port";
    public static final String JETTY_HTTPS_PORT = "jetty.server.https.port";
    public static final String JETTY_MIN_THREAD = "jetty.server.threadpool.minthread";
    public static final String JETTY_MAX_THREAD = "jetty.server.threadpool.maxthread";

    public static final String JETTY_DISPATCHER_TIMEOUT = "jetty.proxy.dispatcher.timeout";

    public static final String JETTY_SSL_KEYSTORE_PATH = "jetty.ssl.keystore.path";
    public static final String JETTY_SSL_KEYSTORE_PASSWORD = "jetty.ssl.keystore.password";
    public static final String JETTY_SSL_KEYSTORE_TYPE = "jetty.ssl.keystore.type";
    public static final String JETTY_SSL_KEYMANAGER_PASSWORD = "jetty.ssl.keymanager.password";
    public static final String JETTY_SSL_TRUSTSTORE_PATH = "jetty.ssl.truststore.path";


    public static final String JETTY_DISPATCHER_URL = "j_dis_url";

    public static final int END_REQUEST = 20000;
}
