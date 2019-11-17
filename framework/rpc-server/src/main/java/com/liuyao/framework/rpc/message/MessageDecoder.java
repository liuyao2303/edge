package com.liuyao.framework.rpc.message;

import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;
import com.liuyao.framework.rpc.deserialize.DeserializerFactory;
import com.liuyao.framework.rpc.deserialize.JsonRpcReqFrame;
import com.liuyao.framework.rpc.deserialize.ObjectDeserialier;
import com.liuyao.framework.rpc.deserialize.RpcRequestMeta;
import com.liuyao.framework.rpc.server.message.RpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(MessageDecoder.class);
    private RpcMessageRequest request;
    public MessageDecoder(RpcMessageRequest request) {
        this.request = request;
    }

    /**
     * 构造出Rpc调用的元数据
     * @return rpcMeta
     */
    public RpcRequestMeta decode() throws RpcException {
        RpcMessage message = request.getRpcMessage();
        if (message == null) {
            return null;
        }

        int rpcType = message.getRpcType();
        byte[] msgData = message.getData();
        String messageId = "23324534534";

        ObjectDeserialier deserialier = DeserializerFactory.getObjectDeserializer(RpcType.from(rpcType));
        if (deserialier == null) {
            throw new RpcException("Rpc failed: failed to load Deserializer");
        }
        try {
            JsonRpcReqFrame frame = deserialier.deserialize(JsonRpcReqFrame.class, msgData);
            RpcRequestMeta rpcRequestMeta = RpcRequestMeta.builder().setMessageId(messageId)
                    .setMethodName(frame.getMethod())
                    .setReturnClassName(frame.getReturnClass())
                    .setServiceClassName(frame.getServiceName())
                    .setParamsInOrder(frame.getParamsInOrder())
                    .build();
            rpcRequestMeta.check();
            return rpcRequestMeta;
        } catch (SerializableException e) {
            LOG.error("Rpc failed: failed to decode message for {}", messageId);
            throw new RpcException("Rpc failed: failed to Deserializer");
        }
    }
}
