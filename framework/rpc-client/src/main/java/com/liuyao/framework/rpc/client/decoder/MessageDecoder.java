package com.liuyao.framework.rpc.client.decoder;

import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;
import org.liuyao.framework.rpc.deserialize.*;
import org.liuyao.framework.rpc.req.RpcMessage;

public class MessageDecoder {
    private RpcMessage rpcMessage;

    public MessageDecoder(RpcMessage rpcMessage) {
        this.rpcMessage = rpcMessage;
    }

    public RpcResponseInstance decode() throws SerializableException, RpcException {
        JsonReponseDeserializer deserialier
                = new JsonReponseDeserializer();

        byte[] stream = rpcMessage.getData();
        RpcResponseInstance frame = deserialier.deserialize(stream);
        return frame;
    }
}
