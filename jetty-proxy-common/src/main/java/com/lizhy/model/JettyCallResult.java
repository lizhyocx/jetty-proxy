package com.lizhy.model;

import java.io.Serializable;

/**
 * NOTE:
 *
 * @author lizhiyang
 * @Date 2019-10-08 9:41
 */
public class JettyCallResult<T> implements Serializable {
    public static final int CODE_FAILURE = -1;
    public static final int CODE_SUCCESS = 1;

    private final boolean success;
    private final int code;
    private final String msg;
    private final T resultObject;

    public JettyCallResult(boolean isSuccess, int code, String msg, T resultObject){
        this.success = isSuccess;
        this.code = code;
        this.msg = msg;
        this.resultObject = resultObject;
    }


    public static <T> JettyCallResult<T> success(){
        return new JettyCallResult<T>(true,CODE_SUCCESS,"default success",null);
    }

    public static <T> JettyCallResult<T> success(T resultObject){
        return new JettyCallResult<T>(true,CODE_SUCCESS,"default success",resultObject);
    }

    public static <T> JettyCallResult<T> success(int code, String msg, T resultObject){
        return new JettyCallResult<T>(true,code,msg,resultObject);
    }

    public static <T> JettyCallResult<T> failure(){
        return new JettyCallResult<T>(false,CODE_FAILURE,"default failure",null);
    }

    public static <T> JettyCallResult<T> failure(String msg){
        return new JettyCallResult<T>(false,CODE_FAILURE,msg,null);
    }

    public static <T> JettyCallResult<T> failure(int code, String msg){
        return new JettyCallResult<T>(false,code,msg,null);
    }

    @Override
    public String toString(){
        String result = new StringBuilder()
                .append('{')
                .append("\"success\":").append(success).append(',')
                .append("\"code\":").append(code).append(',')
                .append("\"msg\":\"").append(msg).append("\",")
                .append("\"resultObject\":").append(resultObject)
                .append('}')
                .toString();
        return result;
    }

    /* -----------------------------getters&setters------------------------------- */

    public boolean hasData(){
        return resultObject!=null;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getResultObject() {
        return resultObject;
    }
}
