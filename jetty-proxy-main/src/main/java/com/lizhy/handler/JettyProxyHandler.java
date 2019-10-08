package com.lizhy.handler;

import com.lizhy.config.JettyProxyConfig;
import com.lizhy.constants.ProjectConstants;
import com.lizhy.handler.pre.AccessAmountPreHandler;
import com.lizhy.handler.pre.InnerRequestPreHandler;
import com.lizhy.http.JettyHttpClient;
import com.lizhy.http.JettyHttpProxyWorker;
import com.lizhy.http.JettyProxyResponseManager;
import com.lizhy.model.JettyCallResult;
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

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if(response.isCommitted() || baseRequest.isHandled()) {
            return;
        }
        ;baseRequest.setHandled(true);
        logger.info("receive target:"+target+",request queryString:"+request.getQueryString());

        JettyProxyContext context = JettyProxyUtils.setJettyProxyContext(request);
        context.setRequest(request);
        context.setResponse(response);
        logger.info("JettyProxyContext:"+context);


        AccessAmountPreHandler accessAmountPreHandler = new AccessAmountPreHandler();
        InnerRequestPreHandler innerRequestPreHandler = new InnerRequestPreHandler(response);
        accessAmountPreHandler.setNextHandler(innerRequestPreHandler);
        JettyCallResult<Boolean> result = accessAmountPreHandler.doHandler(context);
        if(result == null || !result.isSuccess()) {
            logger.warn("pre handler fail, result={}", result);
            return;
        }
        if(result.getCode() == ProjectConstants.END_REQUEST) {
            return;
        }

        //开启异步
        request.startAsync();
        JettyProxyResponseManager responseManager = new JettyProxyResponseManager(context);
        JettyHttpProxyWorker worker = new JettyHttpProxyWorker(responseManager, context, config);
        JettyHttpClient.getInstance().getThreadPool().execute(worker);
    }
}
