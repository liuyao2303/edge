package com.liuyao.framework.rpc.server;

import com.liuyao.framework.rpc.server.handler.RpcFrameDecoder;
import com.liuyao.framework.rpc.server.handler.RpcMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class NettyRpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(NettyRpcServer.class);

    //要绑定的端口号和ip地址
    private int port;
    private String address;
    private Optional<Integer> workProcessers;
    private Channel channel;
    ServerBootstrap serverBootstrap;
    private RpcServerOption option = new RpcServerOption();

    private NioEventLoopGroup selectLoopGroup;
    private NioEventLoopGroup workerEventLoop;

    public NettyRpcServer(String address, int port, RpcServerOption option) {
        this(address, port, 4, option);
    }

    public NettyRpcServer(String address, int port, int workers, RpcServerOption option) {
        this.port = port;
        this.address = address;
        this.workProcessers = Optional.of(workers);
        this.option.copyFrom(option);
    }

    /**
     * 启动服务
     */
    public void start() {
        selectLoopGroup = new NioEventLoopGroup(1);
        workerEventLoop = new NioEventLoopGroup(workProcessers.orElse(4));
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(selectLoopGroup, workerEventLoop)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address, port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new RpcFrameDecoder());
                            socketChannel.pipeline().addLast(new RpcMessageHandler());
                        }
                    });
            ChannelFuture closeFuture = serverBootstrap.bind().sync();
            channel = closeFuture.channel();
            LOG.info("Rpc Server bind net {} port {} on channel :: {}", address, port, closeFuture.channel());
        } catch (InterruptedException e) {
            LOG.error("server start failed :: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭服务器
     */
    public void stop() {
        channel.close().addListener((ChannelFutureListener) future -> LOG.info("server closed"));
    }

}
