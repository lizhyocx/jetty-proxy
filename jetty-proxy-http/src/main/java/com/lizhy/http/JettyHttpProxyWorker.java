package com.lizhy.http;

import com.lizhy.config.JettyProxyConfig;
import com.lizhy.model.JettyProxyContext;
import com.lizhy.util.JettyProxyUtils;
import com.lizhy.util.ParamUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * jetty 请求转发工作类
 * Created by lizhiyang on 2017-04-26 14:25.
 */
public class JettyHttpProxyWorker implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(JettyHttpProxyWorker.class);
    private JettyProxyResponseManager responseManager;
    private JettyProxyContext context;
    private JettyProxyConfig config;

    public JettyHttpProxyWorker(JettyProxyResponseManager responseManager, JettyProxyContext context, JettyProxyConfig config) {
        this.responseManager = responseManager;
        this.context = context;
        this.config = config;
    }
    @Override
    public void run() {
        String dispatcherUrl = context.getDispatcherUrl();
        if(StringUtils.isBlank(dispatcherUrl)) {
            logger.warn("dispatcherUrl is blank");
            return;
        }
        if(dispatcherUrl.contains("?")) {
            dispatcherUrl += "&" + ParamUtils.getFormEncodedString(context.getUrlParameterMap(), context.getCharset());
        } else {
            dispatcherUrl += "?" + ParamUtils.getFormEncodedString(context.getUrlParameterMap(), context.getCharset());
        }
        HttpClient client = JettyHttpClient.getInstance().getHttpClient();
        Request req = client.newRequest(dispatcherUrl).timeout(config.getDispatcherTimeout(), TimeUnit.SECONDS)
                .method(context.getRequest().getMethod());// 设置超时
        JettyProxyUtils.setJettyProxyRequest(req, context);
        req.send(new BufferingResponseListener(8 * 1024 * 1024){
            @Override
            public void onComplete(Result result) {
                if(result.isSucceeded()) {
                    byte[] content = getContent();
                    responseManager.onComplete();
                }else {
                    logger.error("http call error:{},{}",result.getFailure(),result.getResponseFailure());
                    // 目标地址不可达时，定义统一异常返回
                    responseManager.onFail();
                }
            }

        });
    }
}
