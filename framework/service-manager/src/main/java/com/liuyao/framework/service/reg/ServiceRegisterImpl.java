package com.liuyao.framework.service.reg;

import com.google.common.collect.Maps;
import com.liuyao.framework.lang.AppException;
import com.liuyao.framework.service.meta.ServiceInfMeta;

import java.util.Map;

public class ServiceRegisterImpl implements ServiceRegister {

    private static final ServiceRegister INSTANCE = new ServiceRegisterImpl();
    private final Map<Class, ServiceInfMeta> serviceMap = Maps.newConcurrentMap();

    public static ServiceRegister getInstance() {
        return INSTANCE;
    }

    @Override
    public <T> void registerService(Class<T> serviceClass, T serviceImpl) {
        ServiceInfMeta meta = ServiceInfMeta.builder().setService(serviceClass, serviceImpl).builder();
        serviceMap.put(serviceClass, meta);
    }

    @Override
    public boolean isServicePresent(Class serviceClass) {
        return serviceMap.containsKey(serviceClass);
    }

    @Override
    public void unregister(Class serviceClass) {
        serviceMap.remove(serviceClass);
    }

    public <T> void registerService(String qualifiedClassName, T serviceImpl) {
        try {
            Class cls = Class.forName(qualifiedClassName);
            ServiceInfMeta meta = ServiceInfMeta.builder().setService(cls, serviceImpl).builder();
            serviceMap.put(cls, meta);
        } catch (ClassNotFoundException e) {
            throw new AppException("Register rpc service failed", e);
        }
    }

    @Override
    public ServiceInfMeta getServiceMeta(Class serviceClass) {
        return serviceMap.get(serviceClass);
    }

    @Override
    public ServiceInfMeta getServiceMeta(String serviceClassName) {
        try {
            Class cls = Class.forName(serviceClassName);
            return serviceMap.get(cls);
        } catch (ClassNotFoundException e) {
            throw new AppException("Failed to fetch Service Meta", e);
        }
    }
}
