package com.liuyao.framework.rpc.client.handler;

import com.liuyao.framework.rpc.client.channel.NettyChannelClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.liuyao.framework.rpc.req.RpcMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelRpcDeframeHandler extends MessageToByteEncoder {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelRpcDeframeHandler.class);

    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //只处理RpcMessageRequest消息类型
        if (! (msg instanceof RpcMessageRequest)) {
            LOG.error("drop unrelated message {}", msg);
        }

        //写入头部
        out.writeLong(0x5AA5);
        RpcMessageRequest request = (RpcMessageRequest) msg;
        out.writeInt(request.getRpcMessage().getData().length);
        String messageId = request.getMessageId();
        //写入messageId
        out.writeBytes(messageId.getBytes());
        out.writeBytes(request.getRpcMessage().getData());
    }

}
