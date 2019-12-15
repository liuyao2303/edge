package com.liuyao.framework.rpc.client.channel;

import com.liuyao.framework.lang.AppException;
import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.rpc.client.handler.ChannelDetectHandler;
import com.liuyao.framework.rpc.client.handler.ChannelRpcDeframeHandler;
import com.liuyao.framework.rpc.client.handler.RpcFrameDecoder;
import com.liuyao.framework.rpc.client.handler.RpcMessageInHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.liuyao.framework.rpc.req.RpcMessageRequest;

public class NettyChannelClient {

    private EventLoopGroup workerGroup = new NioEventLoopGroup(1);
    private String host;
    private int port;
    private Channel channel;

    public NettyChannelClient() {

    }

    public NettyChannelClient(String address, int port) {
        this.host = address;
        this.port = port;
    }

    public void stop() {
        channel.close();
        workerGroup.shutdownGracefully();
    }

    public void start() throws RpcException {
        start(host, port);
    }

    public void writeMessage(RpcMessageRequest messageRequest) throws RpcException {
        if (channel == null) {
            throw new RpcException("channel not initialized");
        }
        channel.writeAndFlush(messageRequest);
    }

    public void start(String host, int port) throws RpcException {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelDetectHandler(NettyChannelClient.this));
                    ch.pipeline().addLast(new ChannelRpcDeframeHandler());
                    ch.pipeline().addLast(new RpcFrameDecoder());
                    ch.pipeline().addLast(new RpcMessageInHandler());
                }
            });

            // Start the client.
            ChannelFuture channelFuture = b.connect(host, port).sync(); // (5)
            if (channelFuture.isSuccess()) {
                this.channel = channelFuture.channel();
            }
            channelFuture.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println(future.channel());
                }
            });
        } catch (Exception e) {
            throw new RpcException("Failed to start rpc channel client", e);
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void channelRegister(Channel channel) {
        this.channel = channel;
    }

    /**
     * 链路断链
     */
    public void channelUnregister() {
        this.channel = null;
    }
}
