package org.liuyao.framework.rpc.deserialize;

import org.liuyao.framework.rpc.type.RpcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeserializerFactory {

    private static final Logger LoG = LoggerFactory.getLogger(DeserializerFactory.class);

    public static ObjectDeserialier getObjectDeserializer(RpcType rpcType) {
        if (rpcType == RpcType.JSON) {
            return new JsonRequestDeserializer();
        } else if (rpcType == RpcType.HEASON) {
            return null;
        }

        LoG.error("cannot find rpc Deserializer for {}, use default", rpcType);
        return new JsonRequestDeserializer();
    }
}
