package com.lizhy.handler.pre;

import com.lizhy.constants.ProjectConstants;
import com.lizhy.model.JettyCallResult;
import com.lizhy.model.JettyProxyContext;
import com.lizhy.util.CacheUtils;
import com.lizhy.util.JettyProxyUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * NOTE: 内部请求：获取非法请求ip，是否是非法请求，健康检查的请求
 *
 * @author lizhiyang
 * @Date 2018-07-12 15:13
 */
public class InnerRequestPreHandler extends AbstractPreHandler {
    private HttpServletResponse response;
    public InnerRequestPreHandler(HttpServletResponse response) {
        this.response = response;
    }
    @Override
    public JettyCallResult<Boolean> doHandler(JettyProxyContext requestContext) {
        try {
            if(getIllegalRequestIp(requestContext)
                    || healthCheck(requestContext)) {
                //结束请求，不再继续执行
                return JettyCallResult.success(ProjectConstants.END_REQUEST, "END_REQUEST", true);
            }
            if(illegalRequest(requestContext)) {
                return JettyCallResult.failure("BLACK_IP");
            }
            if(nextHandler != null) {
                return nextHandler.doHandler(requestContext);
            }
        } catch (Exception e) {
            logger.error("InnerRequestHandler exception", e);
        }
        return JettyCallResult.success(true);
    }

    /**
     * 是否是非法请求
     * @param requestContext
     * @return
     * @throws Exception
     */
    private boolean illegalRequest(JettyProxyContext requestContext) throws Exception{
        // 是否是非法请求
        String key = requestContext.getRequestIP()+"";
        String ifIllegal = CacheUtils.getIfPresent(key);
        if(StringUtils.isNotBlank(ifIllegal)){
            return true;
        }
        return false;
    }

    /**
     * 获取非法请求ip
     * @param requestContext
     * @return
     * @throws Exception
     */
    private boolean getIllegalRequestIp(JettyProxyContext requestContext) {
        if(!"/getIllRequestIp.do".equals(requestContext.getRequestURI())) {
            return false;
        }
        ConcurrentMap map = new ConcurrentHashMap();
        JettyProxyUtils.handleResponse(response,map.toString(),null,logger);
        return true;
    }

    /**
     * 判断请求是否为健康检查,并返回相关信息
     * @author: hejinen
     * @date:2017年1月6日 上午10:24:38
     */
    private boolean healthCheck(JettyProxyContext requestContext) {
        if(!"/healthCheck.do".equals(requestContext.getRequestURI())) {
            return false;
        }
        JettyProxyUtils.handleResponse(response,"ok",null,logger);
        return true;
    }
}
