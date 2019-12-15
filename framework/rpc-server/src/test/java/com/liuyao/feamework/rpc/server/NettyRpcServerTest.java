package com.liuyao.feamework.rpc.server;

import com.liuyao.framework.rpc.server.NettyRpcServer;
import com.liuyao.framework.service.api.Animinal;
import com.liuyao.framework.service.api.Dog;
import com.liuyao.framework.service.reg.ServiceRegister;
import com.liuyao.framework.service.reg.ServiceRegisterImpl;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class NettyRpcServerTest {

    @Test
    public void testServerStart() throws InterruptedException {
        ServiceRegister serviceRegister = ServiceRegisterImpl.getInstance();
        serviceRegister.registerService(Animinal.class, new Dog());

        NettyRpcServer server = new NettyRpcServer("127.0.0.1", 8089, null);
        server.start();
        TimeUnit.SECONDS.sleep(10000000);
    }

}
