package com.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 *
 */
public class JsonRpcRespFrame {


    @JsonProperty("errorCode")
    private int errorCode;

    @JsonProperty("errorMsg")
    private String errorMsg;

    @JsonProperty("service")
    private String serviceName;

    @JsonProperty("method")
    private String method;

    @JsonProperty("resultClass")
    private String returnClass;

    @JsonProperty("result")
    private Object result;

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

    public String getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
