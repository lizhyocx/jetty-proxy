package com.lizhy.server;

import com.lizhy.config.JettyProxyConfig;
import com.lizhy.handler.JettyProxyHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
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

    public void startServer() {
        BlockingDeque<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<Runnable>(500);
        QueuedThreadPool threadPool = new QueuedThreadPool(config.getMinServerThread(), config.getMaxServerThread(), 60000, linkedBlockingDeque);
        threadPool.setName("JettyProxyServer ThreadPool");
        server = new Server(threadPool);

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(config.getHttpPort());

        server.addConnector(connector);
        server.setHandler(new JettyProxyHandler(config));
    }

    public void stopServer() throws Exception {
        if(server != null) {
            server.stop();
        }
    }

}
