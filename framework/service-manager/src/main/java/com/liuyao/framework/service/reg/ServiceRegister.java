package com.liuyao.framework.service.reg;

import com.liuyao.framework.service.meta.ServiceInfMeta;

public interface ServiceRegister {

    /**
     * 注册服务
     * @param serviceClass class
     * @param serviceImpl 服务实例
     * @param <T> 服务泛型
     */
    <T> void registerService(Class<T> serviceClass, T serviceImpl);

    /**
     * 服务是否已经注册
     * @param serviceClass 注册的服务接口
     * @return 服务实例
     */
    boolean isServicePresent(Class serviceClass);

    /**
     * 反注册
     * @param serviceClass 反注册实例
     */
    void unregister(Class serviceClass);

    /**
     * 根据类型名称，注册相应的服务
     * @param qualifiedClassName className
     * @param serviceImpl 服务是实例
     * @param <T> 反省
     */
    <T> void registerService(String qualifiedClassName, T serviceImpl);

    /**
     * 获取服务元信息
     * @param ServiceClass 服务类型
     */
    ServiceInfMeta getServiceMeta(Class ServiceClass);

    /**
     * 获取服务是实例
     * @param serviceClassName 类型名称
     * @return serviceMeta
     */
    ServiceInfMeta getServiceMeta(String serviceClassName);
}
