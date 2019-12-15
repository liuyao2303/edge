package com.liuyao.framework.rpc.caller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuyao.framework.lang.RpcException;
import org.liuyao.framework.rpc.resp.RpcRespMessage;
import io.netty.channel.ChannelHandlerContext;
import org.liuyao.framework.rpc.deserialize.JsonRpcRespFrame;
import org.liuyao.framework.rpc.deserialize.RpcRequestMeta;
import com.liuyao.framework.rpc.message.MessageDecoder;
import com.liuyao.framework.rpc.message.MessageEncoder;
import org.liuyao.framework.rpc.req.RpcMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class MessageDecoderTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MessageDecoderTask.class);
    private RpcMessageRequest request;

    public MessageDecoderTask(RpcMessageRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        ChannelHandlerContext ctx = request.getCtx();
        if (ctx.isRemoved()) {
            return;
        }

        processRpcMessage(request);
    }

    private void processRpcMessage(RpcMessageRequest request) {
        RpcRequestMeta meta = null;
        try {
            MessageDecoder decoder = new MessageDecoder(request);
            meta = decoder.decode();
            meta.check();
            RpcCaller caller = new RpcCaller(meta);
            Optional result = caller.call();
            writeMessageResult(request, meta, result);
        } catch (RpcException e) {
            if (!(meta == null)) {
                writeRpcError(request, meta, e);
                return;
            }
            LOG.error("Rpc error: request deserializer failed");
        }
    }

    /**
     * 向对方写入rpc远程调用的结果
     * @param request 请求
     * @param meta 请求元数据
     * @param result 调用结果
     */
    private void writeMessageResult(RpcMessageRequest request, RpcRequestMeta meta, Optional result) {
        JsonRpcRespFrame frame = new JsonRpcRespFrame();
        frame.setMethod(meta.getMethod());
        frame.setServiceName(meta.getServiceClassName());
        frame.setReturnClass(meta.getReturnClassName());
        frame.setErrorCode(0);
        if (meta.hasReturnType()) {
            frame.setResult(result.get());
        }
        MessageEncoder messageEncoder = new MessageEncoder(frame);
        try {
            byte[] raw =  messageEncoder.decode();
            RpcRespMessage respMessage = getRpcRespMessage(request, meta, raw);
            request.getCtx().channel().writeAndFlush(respMessage);
        } catch (JsonProcessingException e) {
            writeRpcError(request, meta, new RpcException("decoder failed"));
        }
    }

    /**
     * 像对方写入rpc调用异常信息
     * @param request qingqiu
     * @param e 错误信息
     */
    private void writeRpcError(RpcMessageRequest request, RpcRequestMeta meta, Exception e) {
        JsonRpcRespFrame frame = new JsonRpcRespFrame();
        frame.setMethod(meta.getMethod());
        frame.setServiceName(meta.getServiceClassName());
        frame.setReturnClass(meta.getReturnClassName());
        frame.setErrorCode(1023);
        frame.setErrorMsg(e.getMessage());
        MessageEncoder messageEncoder = new MessageEncoder(frame);
        byte[] raw = new byte[0];
        try {
            raw = messageEncoder.decode();
        } catch (JsonProcessingException ex) {
            LOG.error("Failed to send error Message");
        }
        RpcRespMessage respMessage = getRpcRespMessage(request, meta, raw);
        request.getCtx().channel().writeAndFlush(respMessage);
    }

    private RpcRespMessage getRpcRespMessage(RpcMessageRequest request, RpcRequestMeta meta, byte[] raw) {
        RpcRespMessage respMessage = new RpcRespMessage();
        respMessage.setMessageId(meta.getMessageID());
        respMessage.setRpcType(request.getRpcMessage().getRpcType());
        respMessage.setRawBytes(raw);
        return respMessage;
    }

}
