package com.liuyao.framework.service.meta;

import com.google.common.collect.Maps;
import com.liuyao.framework.lang.AppException;
import com.liuyao.framework.lang.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 *
 * 保存注册的接口类型元数据
 * 实例保存，不用每次启动时候动态加载
 *
 */
public class ServiceInfMeta {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceInfMeta.class);
    private Class serverInfCls;
    private Object serviceImpl;
    private Map<String, MethodMeta> methodDataMap = Maps.newConcurrentMap();


    public static class Builder {
        ServiceInfMeta meta = new ServiceInfMeta();

        public <T> Builder setService(Class<T> serviceCls, T serviceImpl) {
            if (serviceCls == null || serviceImpl == null) {
                throw new AppException("Failed register rpc service for " + serviceCls);
            }

            meta.serverInfCls = serviceCls;
            meta.serviceImpl = serviceImpl;
            return this;
        }

        public ServiceInfMeta builder() {
            Map<String, MethodMeta> methodMetaMap = Maps.newConcurrentMap();
            Class cls = meta.serverInfCls;
            LOG.info("builder rpc meta for {}", meta.serverInfCls.getName());
            Method[] methods = cls.getDeclaredMethods();
            Arrays.stream(methods).forEach(method -> {
                MethodMeta methodMeta = MethodMeta.builder().setMethod(method).builder();
                if (methodMeta != null) {
                    methodMetaMap.putIfAbsent(method.getName(), methodMeta);
                }
            });
            meta.methodDataMap.putAll(methodMetaMap);
            return meta;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional invoke(String method, Object[] args) throws ExecutionException {
        MethodMeta methodMeta = methodDataMap.get(method);
        if (methodMeta == null) {
            throw new ExecutionException(String.format("method call for %s is not present", method));
        }

        return methodMeta.invoke(serviceImpl, args);
    }

    public Optional invoke(String method) throws ExecutionException {
        MethodMeta methodMeta = methodDataMap.get(method);
        if (methodMeta == null) {
            throw new ExecutionException(String.format("method call for %s is not present", method));
        }

        return methodMeta.invoke(serviceImpl);
    }

}
