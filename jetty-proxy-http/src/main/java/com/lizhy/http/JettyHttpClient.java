package com.lizhy.http;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Created by lizhiyang on 2017-04-26 14:20.
 */
public class JettyHttpClient {
    private static Logger logger = LoggerFactory.getLogger(JettyHttpClient.class);
    private static JettyHttpClient instance = new JettyHttpClient();

    private static HttpClient client;
    private static QueuedThreadPool threadPool;

    private JettyHttpClient() {
        SslContextFactory sslContextFactory = new SslContextFactory(true);
        client = new HttpClient(sslContextFactory);
        client.setMaxRequestsQueuedPerDestination(1024);
        client.setMaxConnectionsPerDestination(1024*2);

        BlockingQueue<Runnable> queue = new BlockingArrayQueue<>(500);
        QueuedThreadPool clientThreadPool = new QueuedThreadPool(8, 10, 60000, queue);
        clientThreadPool.setName("JettyHttpClientPool");
        client.setExecutor(clientThreadPool);
        try {
            client.start();
        } catch (Exception e) {
            logger.error("JettyHttpClient start exception", e);
        }

        threadPool = new QueuedThreadPool();
        threadPool.setName("JettyProxyHandlerThreadPool");
        threadPool.setDaemon(true);
        threadPool.setMaxThreads(10);
        threadPool.setMinThreads(8);
        try {
            logger.info("异步调用线程池 QueuedThreadPool start");
            threadPool.start();
        } catch (Exception e) {
            logger.error("QueuedThreadPool 初始化异常",e);
        }
    }

    public static JettyHttpClient getInstance() {
        return instance;
    }

    public HttpClient getHttpClient() {
        return client;
    }

    public QueuedThreadPool getThreadPool() {
        return threadPool;
    }
}
