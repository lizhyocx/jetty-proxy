package com.lizhy.model;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * jetty请求上下文信息
 * Created by lizhiyang on 2017-04-26 14:45.
 */
public class JettyProxyContext implements Serializable {
    private static final long serialVersionUID = -5582085897172928953L;
    private HttpServletRequest request;
    private HttpServletResponse response;
    //接收请求时间
    private Long requestTime;
    //请求转发时间
    private Long sendTime;
    //客户端请求IP
    private String requestIP;
    private String requestURI;
    //转发url地址，从url参数中获取
    private String dispatcherUrl;
    //url中的参数信息（去除转发路径）
    private Map<String, String[]> urlParameterMap;
    //body体中的参数信息
    private Map<String, String[]> bodyParameterMap;
    //所有的参数信息（url和body体）
    private Map<String, String[]> parameterMap;
    private Map<String, String[]> headerMap;
    //请求编码
    private String charset;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getRequestIP() {
        return requestIP;
    }

    public void setRequestIP(String requestIP) {
        this.requestIP = requestIP;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public Map<String, String[]> getUrlParameterMap() {
        return urlParameterMap;
    }

    public void setUrlParameterMap(Map<String, String[]> urlParameterMap) {
        this.urlParameterMap = urlParameterMap;
    }

    public Map<String, String[]> getBodyParameterMap() {
        return bodyParameterMap;
    }

    public void setBodyParameterMap(Map<String, String[]> bodyParameterMap) {
        this.bodyParameterMap = bodyParameterMap;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getDispatcherUrl() {
        return dispatcherUrl;
    }

    public void setDispatcherUrl(String dispatcherUrl) {
        this.dispatcherUrl = dispatcherUrl;
    }

    public Map<String, String[]> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String[]> headerMap) {
        this.headerMap = headerMap;
    }

    @Override
    public String toString() {
        try {
            String str = ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
            StringBuilder builder = new StringBuilder(str);
            builder.append(";headerMap:").append(JSON.toJSONString(headerMap));
            builder.append(";urlParameterMap:").append(JSON.toJSONString(urlParameterMap));
            builder.append(";bodyParameterMap:").append(JSON.toJSONString(bodyParameterMap));
            builder.append(";parameterMap:").append(JSON.toJSONString(parameterMap));
            return builder.toString();
        } catch (Exception e) {
            return super.toString();
        }
    }
}
