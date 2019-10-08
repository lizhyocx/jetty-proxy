package com.lizhy.handler.pre;

import com.lizhy.model.JettyCallResult;
import com.lizhy.model.JettyProxyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NOTE: 请求转发前置处理器，用于鉴权、规则匹配等
 *
 * @author lizhiyang
 * @Date 2018-07-12 14:14
 */
public abstract class AbstractPreHandler {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractPreHandler.class);
    //下一个处理器
    protected AbstractPreHandler nextHandler;

    public abstract JettyCallResult<Boolean> doHandler(final JettyProxyContext requestContext);

    public void setNextHandler(AbstractPreHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
