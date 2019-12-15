package com.liuyao.framework.rpc.client.req;

import org.liuyao.framework.rpc.req.RpcMessage;

import java.util.concurrent.Future;

public interface MessageProcess {
    Future<RpcMessage> get();
    void whenComplete(RpcMessage rpcMessage);
    void whenRpcError(String messageId, Exception e);
    void whenChannelClosed();
}
