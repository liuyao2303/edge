package com.liuyao.framework.rpc.server.handler;

import com.liuyao.framework.rpc.server.message.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RpcFrameDecoder extends ByteToMessageDecoder {

    private static final long START_BYTE = 0x5AA5;
    private static final Logger LOG = LoggerFactory.getLogger(RpcFrameDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        long headerInt;
        int skips = 0;
        while (true) {
            in.markReaderIndex();
            //没有可读的字节数了, 等到下一次再进行处理
            if (in.readableBytes() < 8) {
                return;
            }
            long header = in.readLong();
            if (START_BYTE == header) {
                //正好遇到要处理的头部
                headerInt = header;
                break;
            }

            //回到初始状态
            in.resetReaderIndex();
            //跳到下一字节
            in.readByte();
            ++ skips;
        }

        if (skips > 0) {
            LOG.error("skip received useless bytes length {}", skips);
        }

        //获取报文的长度
        int length = in.readInt();
        //到达一帧数据的头部， 开始记录数据
        if (in.readableBytes() < length) {
            //回复初始状态
            in.resetReaderIndex();
            return;
        }

        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setData(readData(in, length));
        rpcMessage.setHeader(headerInt);
        rpcMessage.setRpcType(1);
        rpcMessage.setLength(length);
        out.add(rpcMessage);
    }

    private byte[] readData(ByteBuf in, int length) {
        byte[] data = new byte[length];
        in.readBytes(data, 0, length);
        return data;
    }
}
