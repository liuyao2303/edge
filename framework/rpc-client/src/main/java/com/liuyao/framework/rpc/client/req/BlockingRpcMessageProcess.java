package com.liuyao.framework.rpc.client.req;

import com.google.common.util.concurrent.SettableFuture;
import com.liuyao.framework.lang.AppException;
import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;
import com.liuyao.framework.rpc.client.caller.ServiceCaller;
import com.liuyao.framework.rpc.client.decoder.MessageDecoder;
import org.liuyao.framework.rpc.deserialize.RpcResponseInstance;
import org.liuyao.framework.rpc.req.RpcMessage;

import java.util.concurrent.Future;

public class BlockingRpcMessageProcess implements MessageProcess {
    private ServiceCaller caller;
    private RpcMessage msgRequest;
    private SettableFuture future;

    public BlockingRpcMessageProcess(ServiceCaller caller, RpcMessage rpcMessage) {
        this.caller = caller;
        this.msgRequest = rpcMessage;
    }

    /**
     * 当用户进行调用时，返回future， 用户等待调用结果
     * @return 返回结果
     */
    public Future<RpcMessage> get() {
        SettableFuture<RpcMessage> rpcMessageFuture = SettableFuture.create();
        future = rpcMessageFuture;
        return rpcMessageFuture;
    }

    /**
     * 当出现异常场景时
     * @param rpcMessage msgRequest
     */
    public void whenComplete(RpcMessage rpcMessage) {
        try {
            MessageDecoder decoder = new MessageDecoder(rpcMessage);
            RpcResponseInstance frame = decoder.decode();
            if (frame.getErrorCode() != 0) {
                whenRpcError(rpcMessage.getMessageId(), new AppException(frame.getErrorMsg()));
            }

            Object result = frame.getResult();
            //发送数据
            future.set(result);
        } catch (SerializableException e) {
            whenRpcError(rpcMessage.getMessageId(), new AppException(e.getMessage()));
        } catch (RpcException e) {
            whenRpcError(rpcMessage.getMessageId(), new AppException(e.getMessage()));
        }
    }

    public void whenRpcError(String messageId, Exception e) {
        future.setException(new AppException(String.format("Rpc failed: %s for %s", messageId, e.getMessage())));
    }

    public void whenChannelClosed() {
        future.setException(new AppException(String.format("Rpc failed: msg send failed for %s cause of channel closed",
                msgRequest.getMessageId())));
    }

    public ServiceCaller getCaller() {
        return caller;
    }
}
