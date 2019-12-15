package org.liuyao.framework.rpc.req;

import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

public class RpcMessageRequest {
    private ChannelHandlerContext ctx;  //记录本次请求的handler ctx，用于写入数据
    private String messageId;
    private RpcMessage rpcMessage; //rpc消息信息

    public RpcMessageRequest(ChannelHandlerContext ctx, RpcMessage rpcMessage, String messageId) {
        this.ctx = ctx;
        this.messageId = messageId;
        this.rpcMessage = rpcMessage;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public RpcMessage getRpcMessage() {
        return rpcMessage;
    }

    public void setRpcMessage(RpcMessage rpcMessage) {
        this.rpcMessage = rpcMessage;
    }

    public SocketAddress getRemoteAddr() {
        if (!ctx.isRemoved()) {
            return ctx.channel().remoteAddress();
        }
        return null;
    }

    public String getMessageId() {
        return messageId;
    }

    public boolean isAlive() {
        return ctx.channel().isRegistered();
    }
}
