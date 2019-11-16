package com.liuyao.framework.rpc.message;

import com.liuyao.framework.rpc.deserialize.DeserializerFactory;
import com.liuyao.framework.rpc.meta.RpcMeta;
import com.liuyao.framework.rpc.server.message.RpcMessage;

public class MessageDecoder {

    private RpcMessageRequest request;

    public MessageDecoder(RpcMessageRequest request) {
        this.request = request;
    }

    /**
     * 构造出Rpc调用的元数据
     * @return rpcMeta
     */
    public RpcMeta decode() {
        RpcMessage message = request.getRpcMessage();
        if (message == null) {
            return null;
        }

        int rpcType = message.getRpcType();
        byte[] msgData = message.getData();

        DeserializerFactory.getObjectDeserializer(RpcType.from(rpcType));


        return null;
    }
}
