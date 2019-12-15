package com.liuyao.framework.rpc.client.caller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liuyao.framework.rpc.client.req.BlockingRpcMessageProcess;
import com.liuyao.framework.rpc.client.req.MessageProcess;
import io.netty.channel.Channel;
import org.liuyao.framework.rpc.req.RpcMessage;

import java.util.List;
import java.util.Map;

public class MessageProcessRegister {

    private static final MessageProcessRegister INSTANCE = new MessageProcessRegister();
    private final Map<String, MessageProcess> waitRegisters = Maps.newConcurrentMap();

    public static MessageProcessRegister getInstance() {
        return INSTANCE;
    }

    public void registerProcess(Channel channel, RpcMessage rpcMessage) {
        String messageId = rpcMessage.getMessageId();
        ServiceCaller serviceCaller = new ServiceCaller(channel);
        serviceCaller.setRequest(rpcMessage);
        BlockingRpcMessageProcess blockingRpcMessage = new BlockingRpcMessageProcess(serviceCaller, rpcMessage);
        waitRegisters.put(messageId,blockingRpcMessage);
    }

    public void unRegister(String messageId) {
        waitRegisters.remove(messageId);
    }

    public MessageProcess getMessageProcess(String messageId) {
        return waitRegisters.get(messageId);
    }

    public List<MessageProcess> getAll() {
        return Lists.newArrayList(waitRegisters.values());
    }
}
