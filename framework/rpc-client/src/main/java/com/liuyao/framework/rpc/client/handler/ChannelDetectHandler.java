package com.liuyao.framework.rpc.client.handler;

import com.liuyao.framework.rpc.client.channel.NettyChannelClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelDetectHandler extends ChannelInboundHandlerAdapter {

    private NettyChannelClient nettyChannelClient;

    public ChannelDetectHandler(NettyChannelClient nettyChannelClient) {
        this.nettyChannelClient = nettyChannelClient;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        nettyChannelClient.channelUnregister();
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        nettyChannelClient.channelRegister(ctx.channel());
        ctx.fireChannelRegistered();
    }
}
