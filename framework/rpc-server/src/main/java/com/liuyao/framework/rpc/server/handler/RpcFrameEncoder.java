package com.liuyao.framework.rpc.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.liuyao.framework.rpc.resp.RpcRespMessage;

public class RpcFrameEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        //写入头部
        out.writeLong(0x5AA5);
        RpcRespMessage request = (RpcRespMessage) msg;
        String messageId = request.getMessageId();
        //先些length
        out.writeInt(request.getRawBytes().length);
        out.writeBytes(messageId.getBytes());
        out.writeBytes(request.getRawBytes());
    }

}
