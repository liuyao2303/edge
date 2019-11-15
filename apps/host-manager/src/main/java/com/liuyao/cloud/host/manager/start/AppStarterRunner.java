package com.liuyao.cloud.host.manager.start;

import com.liuyao.cloud.host.manager.server.NettyServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStarterRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        NettyServer nettyServer = new NettyServer("0.0.0.0", 8181);
        nettyServer.start();
    }
}
