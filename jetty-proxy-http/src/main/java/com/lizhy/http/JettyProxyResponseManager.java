package com.lizhy.http;

import com.lizhy.model.JettyProxyContext;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpHeaderValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

/**
 * jetty请求转发结果处理类
 * Created by lizhiyang on 2017-04-26 14:35.
 */
public class JettyProxyResponseManager {
    private static Logger logger = LoggerFactory.getLogger(JettyProxyResponseManager.class);

    private JettyProxyContext context;

    public JettyProxyResponseManager(JettyProxyContext context) {
        this.context = context;
    }

    public void onComplete(Result result, byte[] content) {
        try {
            logger.debug("content:"+new String(content));
            context.getResponse().setStatus(result.getResponse().getStatus());
            Iterator<HttpField> it = result.getResponse().getHeaders().iterator();
            while(it.hasNext()){
                HttpField next = it.next();
                context.getResponse().setHeader(next.getName(), next.getValue());
            }
            context.getResponse().getOutputStream().write(content);

            context.getResponse().flushBuffer();
            context.getRequest().getAsyncContext().complete();
        } catch (Exception e) {
            logger.error("onComplete exception", e);
        }

    }

    public void onFail(Result result) {
        HttpServletResponse proxyResponse =  context.getResponse();
        if (proxyResponse.isCommitted())
        {
            try {
                proxyResponse.sendError(-1);
                context.getRequest().getAsyncContext().complete();
            }
            catch (IOException x) {
                logger.error( " could not close the connection", result.getFailure());
            }
        }
        else {
            proxyResponse.resetBuffer();
            if (result.getFailure() instanceof TimeoutException) {
                proxyResponse.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
            } else {
                proxyResponse.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            }
            proxyResponse.setHeader(HttpHeader.CONNECTION.asString(), HttpHeaderValue.CLOSE.asString());
            context.getRequest().getAsyncContext().complete();
        }
    }
}
