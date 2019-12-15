package org.liuyao.framework.rpc.deserialize;

import com.google.common.collect.Maps;
import com.liuyao.framework.lang.RpcException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 请求的元数据类型
 */
public class RpcRequestMeta {

    private static final Map<String, Class> PRIMITIVES = Maps.newHashMap();

    static {
        PRIMITIVES.put("int", int.class);
        PRIMITIVES.put("double", double.class);
        PRIMITIVES.put("char", char.class);
        PRIMITIVES.put("byte", byte.class);
        PRIMITIVES.put("long", long.class);
        PRIMITIVES.put("float", float.class);
        PRIMITIVES.put("short", short.class);
        PRIMITIVES.put("boolean", boolean.class);

    }

    private String messageID;

    /**
     * 数据的请求类型
     */
    private String[] paramsClassNames;

    /**
     * 请求的服务类型
     */
    private String serviceClassName;
    private Class serviceClass;

    /**
     * 要调用的方法
     */
    private String method;

    /**
     * 数据的返回类型
     */
    private String returnClassName;
    private Class returnClass;

    private Object[] params;

    private boolean hasParams() {
        return paramsClassNames != null && paramsClassNames.length > 0;
    }

    public boolean hasReturnType() {
        return !StringUtils.isEmpty(returnClassName);
    }

    /**
     * 获取服务的接口类型
     * @return getServiceClass
     */
    public Class getServiceClass() throws RpcException {

        if (serviceClass != null) {
            return serviceClass;
        }

        try {
            Class cls = Thread.currentThread().getContextClassLoader()
                    .loadClass(serviceClassName);
            serviceClass = cls;
            return cls;
        } catch (ClassNotFoundException e) {
            throw new RpcException(String.format("Rpc Error, Failed to load class for %s",
                    serviceClassName), e);
        }
    }

    /**
     * 获取返回的请求类型
     * @return Class
     */
    public Class getReturnClass() throws RpcException {

        if (returnClass.equals("void")) {
            return null;
        }

        if (PRIMITIVES.containsKey(returnClassName)) {
            return PRIMITIVES.get(returnClassName);
        }

        if (returnClass != null) {
            return returnClass;
        }

        try {
            Class cls = Thread.currentThread().getContextClassLoader()
                    .loadClass(returnClassName);
            returnClass = cls;
            return cls;
        } catch (ClassNotFoundException e) {
            throw new RpcException(String.format("Rpc Error, Failed to load Return Type class for %s",
                    returnClassName), e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        RpcRequestMeta meta = new RpcRequestMeta();

        public Builder setMessageId(String messageId) {
            meta.messageID = messageId;
            return this;
        }

        public Builder setParamsTypeNamesInOrder(String[] paramNames) {
            meta.paramsClassNames = paramNames;
            return this;
        }

        public Builder setReturnClassName(String returnClassName) {
            meta.returnClassName = returnClassName;
            return this;
        }

        public Builder setMethodName(String methodName) {
            meta.method = methodName;
            return this;
        }

        public Builder setServiceClassName(String className) {
            meta.serviceClassName = className;
            return this;
        }

        public Builder setParamsInOrder(Object[] params) {
            meta.params = params;
            return this;
        }

        public RpcRequestMeta build() {
            return meta;
        }
    }

    public void check() throws RpcException {
        if (StringUtils.isEmpty(messageID) || !messageID.matches("([\\d\\w]){10}")) {
            throw new RpcException("Rpc failed: empty or invalid messageId");
        }

        if (getServiceClass() == null) {
            throw new RpcException("Rpc failed: failed to load rpc service");
        }
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParams() {
        return params;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public String getReturnClassName() {
        return returnClassName;
    }

    public String getMessageID() {
        return messageID;
    }
}
