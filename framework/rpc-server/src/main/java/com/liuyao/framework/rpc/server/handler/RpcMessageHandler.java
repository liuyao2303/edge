package com.liuyao.framework.rpc.server.handler;

import com.liuyao.framework.rpc.server.message.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcMessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcMessage) {
            //Handler message here
            System.out.println(msg);

        } else if (msg instanceof ByteBuf) {
            //如果是bb， 则释放资源
            ByteBuf bb = (ByteBuf) msg;
            bb.release();
        }
    }
}
