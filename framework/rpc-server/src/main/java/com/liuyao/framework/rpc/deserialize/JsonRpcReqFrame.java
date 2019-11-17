package com.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 *
 *
 */
public class JsonRpcReqFrame {

    @JsonProperty("service")
    private String serviceName;

    @JsonProperty("method")
    private String method;

    @JsonProperty("resultClass")
    private String returnClass;

    @JsonProperty("params")
    private Map<Integer, Object> params = Maps.newHashMap();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<Integer, Object> getParams() {
        return params;
    }

    public void setParams(Map<Integer, Object> params) {
        this.params = params;
    }

    public String getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
    }

    /**
     * 根据参数顺序获取排序后的参数列表
     * @return 参数列表
     */
    public Object[] getParamsInOrder() {
        if (params.size() <= 0) {
            return null;
        }
        Object[] paramsInOrder = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            paramsInOrder[i] = param;
        }
        return paramsInOrder;
    }
}
