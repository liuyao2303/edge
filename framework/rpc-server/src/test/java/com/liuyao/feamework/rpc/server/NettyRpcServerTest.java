package com.liuyao.feamework.rpc.server;

import com.liuyao.framework.rpc.server.NettyRpcServer;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class NettyRpcServerTest {

    @Test
    public void testServerStart() throws InterruptedException {
        NettyRpcServer server = new NettyRpcServer("127.0.0.1", 8089, null);
        server.start();
        TimeUnit.SECONDS.sleep(10000000);
//        server.stop();
    }

}
