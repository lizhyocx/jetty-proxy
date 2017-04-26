package com.lizhy.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lizhiyang on 2017-04-26 14:55.
 */
public class ParamUtils {
    public static String getValueFromMap(Map<String, String[]> map, String key) {
        if(!ToolUtils.isMapEmpty(map)) {
            String[] values = map.get(key);
            if(values != null && values.length > 0) {
                return values[0];
            }
        }
        return null;
    }
    public static Map<String, String[]> getUrlParamMap(String queryString) {
        if(StringUtils.isBlank(queryString)) {
            return null;
        }
        Map<String, String[]> urlParamMap = new HashMap<String, String[]>();
        if(StringUtils.isNotBlank(queryString)) {
            String[] params = queryString.split("&");
            for (String param : params) {
                String[] arrParam = param.split("=");
                if (arrParam.length > 1) {
                    put2ArrIfExist(urlParamMap, arrParam[0], arrParam[1]);
                } else {
                    if (StringUtils.isNotBlank(arrParam[0])) {
                        if(!urlParamMap.containsKey(arrParam[0])) {
                            urlParamMap.put(arrParam[0], new String[]{""});
                        }
                    }
                }
            }
        }
        return urlParamMap;
    }

    /**
     * 将key和value存入map，如果存在，则用数组存放
     * @param map
     * @param key
     * @param value
     */
    public static void put2ArrIfExist(Map<String, String[]> map, String key, String value) {
        if(StringUtils.isNotBlank(key)) {
            if(map.containsKey(key)) {
                String[] v = map.get(key);
                int newLength = v.length + 1;
                String[] arr = new String[newLength];
                System.arraycopy(v, 0, arr, 0, v.length);
                arr[newLength - 1] = value;
                map.put(key, arr);
            } else {
                value = value == null ? "" : value;
                map.put(key, new String[]{value});
            }
        }
    }

    public static boolean hasValueOfMap(Map<String, String[]> map, String key, String compValue) {
        if(map.containsKey(key)) {
            String[] v = map.get(key);
            for(String value : v) {
                if(value.equals(compValue)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 合并url参数和body参数
     * @param urlParamMap
     * @param bodyParamMap
     * @return
     */
    public static Map<String, String[]> combinationParamMap(Map<String, String[]> urlParamMap, Map<String, String[]> bodyParamMap) {
        Map<String, String[]> map = new HashMap<String, String[]>();
        if(!ToolUtils.isMapEmpty(urlParamMap) && !ToolUtils.isMapEmpty(bodyParamMap)) {
            map.putAll(urlParamMap);
            Iterator<Map.Entry<String, String[]>> iter = bodyParamMap.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<String, String[]> entry = iter.next();
                String key = entry.getKey();
                String[] value = entry.getValue();
                if(map.containsKey(key)) {
                    String[] v = map.get(key);
                    String[] arr = new String[value.length+v.length];
                    System.arraycopy(value, 0, arr, 0, value.length);
                    System.arraycopy(v, 0, arr, value.length, v.length);
                    map.put(key, arr);
                } else {
                    map.put(key, value);
                }
            }
        } else {
            if(!ToolUtils.isMapEmpty(urlParamMap)) {
                map.putAll(urlParamMap);
            }
            if(!ToolUtils.isMapEmpty(bodyParamMap)) {
                map.putAll(bodyParamMap);
            }
        }
        return map;
    }

    public static String getFormEncodedString(Map<String, String[]> param, String charset) {
        if(!ToolUtils.isMapEmpty(param)) {
            StringBuilder builder = new StringBuilder(param.size() * 32);
            Iterator<Map.Entry<String,String[]>> iter = param.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<String, String[]> entry = iter.next();
                String key = entry.getKey();
                String[] arrValue = entry.getValue();
                for(String value : arrValue) {
                    appendParam(builder, key, value, charset);
                }
            }
            return builder.toString();
        }
        return null;
    }

    private static String encode(String value, String charset)
    {
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException x) {
            throw new UnsupportedCharsetException(charset);
        }
    }

    private static void appendParam(StringBuilder builder, String paramKey, String paramValue, String charset) {
        if(builder.length() > 0) {
            builder.append("&");
        }
        builder.append(encode(paramKey, charset)).append("=").append(encode(paramValue, charset));
    }
}
