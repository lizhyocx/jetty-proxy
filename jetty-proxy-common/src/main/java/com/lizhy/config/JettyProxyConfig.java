package com.lizhy.config;

import java.io.Serializable;

/**
 * Created by lizhiyang on 2017-04-26 11:28.
 */
public class JettyProxyConfig implements Serializable {

    private static final long serialVersionUID = 4219725521557271241L;
    private int httpPort;
    private int minServerThread;
    private int maxServerThread;
    private int dispatcherTimeout;

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getMinServerThread() {
        return minServerThread;
    }

    public void setMinServerThread(int minServerThread) {
        this.minServerThread = minServerThread;
    }

    public int getMaxServerThread() {
        return maxServerThread;
    }

    public void setMaxServerThread(int maxServerThread) {
        this.maxServerThread = maxServerThread;
    }

    public int getDispatcherTimeout() {
        return dispatcherTimeout;
    }

    public void setDispatcherTimeout(int dispatcherTimeout) {
        this.dispatcherTimeout = dispatcherTimeout;
    }
}
