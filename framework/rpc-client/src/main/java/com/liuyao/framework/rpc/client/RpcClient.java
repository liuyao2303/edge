package com.liuyao.framework.rpc.client;

import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.rpc.client.channel.NettyChannelClient;
import com.liuyao.framework.rpc.client.poll.MessageProcessPollManger;

public class RpcClient {

    private NettyChannelClient nettyChannelClient;
    private MessageProcessPollManger messageProcessPollManger;

    public void start(String host, int port) throws RpcException {
        nettyChannelClient = new NettyChannelClient(host, port);
        nettyChannelClient.start();
        messageProcessPollManger = MessageProcessPollManger.getInstance();
    }
}
