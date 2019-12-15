package com.liuyao.framework.rpc.client.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.liuyao.framework.rpc.client.caller.MessageProcessRegister;
import com.liuyao.framework.rpc.client.channel.NettyChannelClient;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.liuyao.framework.rpc.deserialize.RpcRequestInstace;
import org.liuyao.framework.rpc.req.RpcMessage;
import org.liuyao.framework.rpc.req.RpcMessageRequest;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ServiceProxy implements MethodInterceptor {

    private NettyChannelClient rpcClient;
    private Class serviceInterface;
    public ServiceProxy(NettyChannelClient rpcClient, Class serviceInterface) {
        this.rpcClient = rpcClient;
        this.serviceInterface = serviceInterface;
    }

    public  <T> T getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(serviceInterface);
        // 设置enhancer的回调对象
        enhancer.setCallback(this);
        // 创建代理对象
        return (T) enhancer.create();
    }

    /**
     * intercept 拦截所有方法，实现rpc调用
     * @param o 对象
     * @param method 方法
     * @param objects 参数
     * @param methodProxy 方法代理
     * @return 调用结果
     * @throws Throwable 方法结果
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        RpcRequestInstace reqFrame = new RpcRequestInstace();
        reqFrame.setMethod(methodName);
        reqFrame.setParams(getParams(objects));
        reqFrame.setServiceName(serviceInterface.getName());
        reqFrame.setReturnClass(methodProxy.getSignature().getReturnType().getClassName());

        RpcMessage message = new RpcMessage();
        message.setData(new ObjectMapper().writeValueAsBytes(reqFrame));
        message.setLength(message.getData().length);
        message.setRpcType(1);
        message.setHeader(0x5AA5);
        message.setMessageId("1111111111");
        MessageProcessRegister register = MessageProcessRegister.getInstance();
        register.registerProcess(rpcClient.getChannel(), message);

        //开始写入数据
        RpcMessageRequest request = new RpcMessageRequest(null, message, message.getMessageId());
        rpcClient.writeMessage(request);

        Future future = register.getMessageProcess(message.getMessageId()).get();
        return future.get(10,TimeUnit.SECONDS);
    }

    private Map<Integer, Object> getParams(Object[] objects) {
        Map<Integer, Object> params = Maps.newHashMap();
        if (objects == null || objects.length == 0) {
            return params;
        }

        for (int i = 0; i < objects.length; i++) {
            params.put(i, objects[i]);
        }
        return params;
    }
}
