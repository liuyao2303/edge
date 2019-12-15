package com.liuyao.framework.rpc.client;

import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.rpc.client.channel.NettyChannelClient;
import com.liuyao.framework.rpc.client.proxy.ServiceProxy;
import com.liuyao.framework.service.api.Animinal;

import java.util.Date;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws RpcException {
        NettyChannelClient channelClient = new NettyChannelClient("127.0.0.1", 8089);
        channelClient.start();
        ServiceProxy serviceProxy = new ServiceProxy(channelClient, Animinal.class);

        Animinal animinal = serviceProxy.getProxyInstance();
//        int r = animinal.earn(123);
//        System.out.println(r);
//
//        animinal.fee("hello huanhuan");

        Map<String, String> map = animinal.getMaps(new Date());
        System.out.println(map);
    }
}
