package com.liuyao.cloud.host.manager.server.inbound;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(ShutdownChannelInboundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            String dataMsg = (String) msg;
            if (dataMsg.contains("shutdown")) {
                ctx.writeAndFlush(ByteBufUtil.writeUtf8(ByteBufAllocator.DEFAULT, "Bye Bye!\n"));
                ctx.channel().close().addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        LOG.info("channel closed");
                    }
                });
            }
            ctx.fireChannelRead(msg);
        }
        ctx.fireChannelRead(msg);
    }
}
