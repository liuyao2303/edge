package com.liuyao.framework.rpc.message;

import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;
import org.liuyao.framework.rpc.deserialize.RpcRequestInstace;
import org.liuyao.framework.rpc.req.RpcMessageRequest;
import org.liuyao.framework.rpc.deserialize.DeserializerFactory;
import org.liuyao.framework.rpc.deserialize.ObjectDeserialier;
import org.liuyao.framework.rpc.deserialize.RpcRequestMeta;
import org.liuyao.framework.rpc.req.RpcMessage;
import org.liuyao.framework.rpc.type.RpcType;
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
        String messageId = request.getMessageId();

        ObjectDeserialier deserialier = DeserializerFactory.getObjectDeserializer(RpcType.from(rpcType));
        if (deserialier == null) {
            throw new RpcException("Rpc failed: failed to load Deserializer");
        }
        try {
            RpcRequestInstace frame = (RpcRequestInstace) deserialier.deserialize(msgData);
            return RpcRequestMeta.builder().setMessageId(messageId)
                    .setMethodName(frame.getMethod())
                    .setReturnClassName(frame.getReturnClass())
                    .setServiceClassName(frame.getServiceName())
                    .setParamsInOrder(frame.getParamsInOrder())
                    .build();
        } catch (SerializableException e) {
            LOG.error("Rpc failed: failed to decode message for {}", messageId);
            throw new RpcException("Rpc failed: failed to Deserializer");
        }
    }
}
