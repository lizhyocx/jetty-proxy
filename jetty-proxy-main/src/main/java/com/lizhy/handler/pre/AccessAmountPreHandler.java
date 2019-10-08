package com.lizhy.handler.pre;

import com.google.common.util.concurrent.RateLimiter;
import com.lizhy.model.JettyCallResult;
import com.lizhy.model.JettyProxyContext;
import com.lizhy.util.RateLimitUtils;

/**
 * NOTE: 访问量统计，QPS监控
 *
 * @author lizhiyang
 * @Date 2018-07-12 14:21
 */
public class AccessAmountPreHandler extends AbstractPreHandler {
    private static final int QPS_THRESHOLD = 1000;
    private static final String QPS_ACCESS_KEY = "QPS_ACCESS_KEY";
    @Override
    public JettyCallResult<Boolean> doHandler(final JettyProxyContext requestContext) {
        //QPS监控
        RateLimiter rateLimiter = RateLimitUtils.getResource(QPS_ACCESS_KEY, QPS_THRESHOLD);
        if(!rateLimiter.tryAcquire()) {
            logger.warn("the request is limit");
        }
        if(nextHandler != null) {
            return nextHandler.doHandler(requestContext);
        }
        return JettyCallResult.success();
    }
}
