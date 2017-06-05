package com.lizhy.config;

import java.io.Serializable;

/**
 * Created by lizhiyang on 2017-04-26 11:28.
 */
public class JettyProxyConfig implements Serializable {

    private static final long serialVersionUID = 4219725521557271241L;
    private int httpPort;
    private int httpsPort;
    private int minServerThread;
    private int maxServerThread;
    private int dispatcherTimeout;

    private String keyStorePath;
    private String keyStorePassword;
    private String keyStoreType;
    private String keyManagerPassword;
    private String trustStorePath;

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
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

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyManagerPassword() {
        return keyManagerPassword;
    }

    public void setKeyManagerPassword(String keyManagerPassword) {
        this.keyManagerPassword = keyManagerPassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }
}
