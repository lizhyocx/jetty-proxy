package com.lizhy.handler;

import com.lizhy.config.JettyProxyConfig;
import com.lizhy.http.JettyHttpClient;
import com.lizhy.http.JettyHttpProxyWorker;
import com.lizhy.http.JettyProxyResponseManager;
import com.lizhy.model.JettyProxyContext;
import com.lizhy.util.JettyProxyUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Http请求接收处理器
 * Created by lizhiyang on 2017-04-26 11:22.
 */
public class JettyProxyHandler extends AbstractHandler {
    private static Logger logger = LoggerFactory.getLogger(JettyProxyHandler.class);

    private JettyProxyConfig config;

    public JettyProxyHandler(JettyProxyConfig config) {
        this.config = config;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //开启异步
        request.startAsync();

        JettyProxyContext context = JettyProxyUtils.setJettyProxyContext(request);

        JettyProxyResponseManager responseManager = new JettyProxyResponseManager();
        JettyHttpProxyWorker worker = new JettyHttpProxyWorker(responseManager, context, config);
        JettyHttpClient.getInstance().getThreadPool().execute(worker);
    }
}
