package com.liuyao.framework.rpc.client.caller;

import io.netty.channel.Channel;
import org.liuyao.framework.rpc.req.RpcMessage;


public class ServiceCaller {

    //要注册的通道信息
    private Channel channel;

    //rpc请求信息
    private RpcMessage request;

    //rpc相应信息
    private RpcMessage response;

    //是否已经注册到线上
    private boolean isRegister = false;

    //请求是否已经完成
    private boolean complete = false;

    public ServiceCaller(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public RpcMessage getRequest() {
        return request;
    }

    public void setRequest(RpcMessage request) {
        this.request = request;
    }

    public RpcMessage getResponse() {
        return response;
    }

    public void setResponse(RpcMessage response) {
        this.response = response;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
