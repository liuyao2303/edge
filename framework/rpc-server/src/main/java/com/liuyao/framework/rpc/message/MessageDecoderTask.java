package com.liuyao.framework.rpc.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

public class MessageDecoderTask implements Runnable {

    private RpcMessageRequest request;

    public MessageDecoderTask(RpcMessageRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        ChannelHandlerContext ctx = request.getCtx();
        if (ctx.isRemoved()) {
            return;
        }

        processRpcMessage(request);
    }

    private void processRpcMessage(RpcMessageRequest request) {
        String message = "来信已收到";
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(message.length());
        byteBuf.writeBytes(message.getBytes());
        request.getCtx().writeAndFlush(byteBuf);
    }
}
