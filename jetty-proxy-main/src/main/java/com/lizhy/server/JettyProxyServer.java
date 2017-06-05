package com.lizhy.server;

import com.lizhy.config.JettyProxyConfig;
import com.lizhy.handler.JettyProxyHandler;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http.CookieCompliance;
import org.eclipse.jetty.http.HttpCompliance;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Jetty服务器，启动一个嵌入式的Jetty
 * Created by lizhiyang on 2017-04-26 11:21.
 */
public class JettyProxyServer {
    private JettyProxyConfig config;
    private Server server;

    public JettyProxyServer(JettyProxyConfig config) {
        this.config = config;
    }

    public void startServer() throws Exception {
        BlockingDeque<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<Runnable>(500);
        QueuedThreadPool threadPool = new QueuedThreadPool(config.getMinServerThread(), config.getMaxServerThread(), 60000, linkedBlockingDeque);
        threadPool.setName("JettyProxyServer ThreadPool");
        server = new Server(threadPool);

        HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setSecurePort(config.getHttpsPort());
        httpConfiguration.setSecureScheme("https");

        ConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfiguration, HttpCompliance.RFC7230);
        ServerConnector httpConnector = new ServerConnector(server, -1, -1, httpConnectionFactory);
        //http端口
        httpConnector.setPort(config.getHttpPort());
        server.addConnector(httpConnector);


        HttpConfiguration sslHttpConfig = new HttpConfiguration(httpConfiguration);
        SecureRequestCustomizer customizer = new SecureRequestCustomizer();
        customizer.setSniHostCheck(true);
        customizer.setStsMaxAge(-1);
        customizer.setStsIncludeSubDomains(false);
        sslHttpConfig.addCustomizer(customizer);

        HTTP2ServerConnectionFactory http2ServerConnectionFactory = new HTTP2ServerConnectionFactory(sslHttpConfig);
        http2ServerConnectionFactory.setMaxConcurrentStreams(1024);
        http2ServerConnectionFactory.setInitialStreamRecvWindow(65535);


        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(config.getKeyStorePath());
        sslContextFactory.setKeyStorePassword(config.getKeyStorePassword());
        sslContextFactory.setKeyStoreType(config.getKeyStoreType());
        sslContextFactory.setKeyManagerPassword(config.getKeyManagerPassword());
        sslContextFactory.setTrustStorePath(config.getTrustStorePath());
        sslContextFactory.setNeedClientAuth(false);
        sslContextFactory.setWantClientAuth(false);
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
        sslContextFactory.setUseCipherSuitesOrder(true);

        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, "alpn");
        ALPNServerConnectionFactory alpnServerConnectionFactory = new ALPNServerConnectionFactory();
        alpnServerConnectionFactory.setDefaultProtocol(httpConnector.getDefaultProtocol());

        ServerConnector sslConnector = new ServerConnector(server, -1, -1, sslConnectionFactory, alpnServerConnectionFactory, http2ServerConnectionFactory);
        sslConnector.setPort(config.getHttpsPort());
        sslConnector.setIdleTimeout(30000);
        sslConnector.setSoLingerTime(-1);
        sslConnector.setAcceptorPriorityDelta(0);
        sslConnector.setAcceptQueueSize(0);

        server.addConnector(sslConnector);

        server.setHandler(new JettyProxyHandler(config));
        server.start();
    }

    public void stopServer() throws Exception {
        if(server != null) {
            server.stop();
        }
    }

}
