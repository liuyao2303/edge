package com.liuyao.framework.service.meta;

import com.liuyao.framework.lang.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class MethodMeta {

    private static final Logger LOG = LoggerFactory.getLogger(MethodMeta.class);

    //方法名称
    private String methodName;
    private Method method;
    //入参个数
    private int inCount;

    //保存的入参的名称, 默认从0， 开始
    private String[] paramNames;
    private Class[] paramTypes;

    public static class Builder {
        MethodMeta methodMeta = new MethodMeta();
        private Method method;

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public MethodMeta builder() {
            methodMeta.methodName = method.getName();
            methodMeta.method = method;

            Class[] parameterTypes = method.getParameterTypes();
            methodMeta.paramNames = new String[parameterTypes.length];
            methodMeta.paramTypes = new Class[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                methodMeta.paramNames[i] = parameterTypes[i].getName();
                methodMeta.paramTypes[i] = parameterTypes[i];
            }
            return methodMeta;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 获取指定位置的参数类型
     * @param index 位置
     * @return 指定位置的参数类型
     */
    public Class paramTypeOfIndex(int index) {
        if (paramTypes.length > 0 && index >= 0 && index < paramTypes.length) {
            return paramTypes[index];
        }

        return null;
    }

    /**
     * 在指定的位置上调用相应的服务
     * @param serviceImpl 服务实例
     * @param args 参数列表，顺序很重要
     * @return 返回调用结果
     */
    public Optional invoke(Object serviceImpl, Object[] args) throws ExecutionException {

        if (!hasParams()) {
            throw new ExecutionException("wrong params");
        }

        try {
            Object resp = method.invoke(serviceImpl, args);
            return resp == null ? Optional.empty() : Optional.of(resp);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ExecutionException("execution failed", e);
        }
    }

    /**
     * 在指定的位置上调用相应的服务
     * @param serviceImpl 服务实例
     * @return 返回调用结果
     */
    public Optional invoke(Object serviceImpl) throws ExecutionException {
        if (hasParams()) {
            throw new ExecutionException("wrong params");
        }
        try {
            check(new Object[0]);
            Object resp = method.invoke(serviceImpl);
            return resp == null ? Optional.empty() : Optional.of(resp);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ExecutionException("execution failed", e);
        }
    }

    private boolean hasParams() {
        return paramTypes.length > 0;
    }

    /**
     * 检查参数列表是否能够满足
     * @param args 参数列表
     * @throws ExecutionException
     */
    private void check(Object[] args) throws ExecutionException {
        if (args == null || args.length <= 0 || args.length != paramTypes.length) {
            throw new ExecutionException("wrong params");
        }

        for (int i = 0; i < args.length; i++) {
            Class paramType = paramTypes[i];
            if (!args[i].getClass().equals(paramType)) {
                throw new ExecutionException("wrong paramType");
            }

        }
    }
}
