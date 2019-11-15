package com.liuyao.cloud.host.manager.server;

import com.liuyao.cloud.host.manager.server.inbound.EchoChannelInboundHandler;
import com.liuyao.cloud.host.manager.server.inbound.ShutdownChannelInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);

    //要绑定的端口号和ip地址
    private int port;
    private String address;

    private NioEventLoopGroup selectLoopGroup;
    private NioEventLoopGroup workerEventLoop;

    public NettyServer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * 启动服务
     */
    public void start() throws InterruptedException {
        selectLoopGroup = new NioEventLoopGroup(1);
        workerEventLoop = new NioEventLoopGroup(6);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(selectLoopGroup, workerEventLoop)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address, port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addFirst(new EchoChannelInboundHandler(socketChannel));
                            socketChannel.pipeline().addLast(new ShutdownChannelInboundHandler());
                        }
                    });
            ChannelFuture cf = serverBootstrap.bind().sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("server start failed :: ", e);
            throw new RuntimeException(e);
        } finally {
            selectLoopGroup.shutdownGracefully().sync();
        }
    }

}
