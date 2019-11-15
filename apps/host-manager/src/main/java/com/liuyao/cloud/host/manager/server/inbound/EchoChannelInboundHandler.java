package com.liuyao.cloud.host.manager.server.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EchoChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger LOG = LoggerFactory.getLogger(EchoChannelInboundHandler.class);
    private ExecutorService executor = Executors.newCachedThreadPool();

    private SocketChannel socketChannel;

    public EchoChannelInboundHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("channel registered compare :: {}", socketChannel.equals(ctx.channel()));
        ctx.write(ByteBufUtil.writeUtf8(ByteBufAllocator.DEFAULT, "hello world\n"));
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf data = (ByteBuf) msg;
            byte[] datas = new byte[data.readableBytes()];
            data.readBytes(datas);
            String dataMsg = new String(datas);
            if (dataMsg.equals("shutdown\r\n")) {
                ctx.fireChannelRead(dataMsg);
                return;
            }
            LOG.info("channel read data :: {} thread :: {}", dataMsg, Thread.currentThread());
            executor.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    LOG.info("do my business in :: {} for input :: {}", Thread.currentThread(), dataMsg);
                    ctx.writeAndFlush(ByteBufUtil.writeUtf8(ByteBufAllocator.DEFAULT, "Message has been consumed\n"));
                } catch (InterruptedException e) {
                    LOG.error("business interrupted");
                }
            });
            data.release();
        } else {
            System.out.println("fire shutdown");
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LOG.info("channelReadComplete");
        ctx.fireChannelReadComplete();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("error occur :: ", cause);
    }
}
