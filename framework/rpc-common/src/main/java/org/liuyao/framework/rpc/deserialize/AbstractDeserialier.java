package org.liuyao.framework.rpc.deserialize;

import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;

public abstract class AbstractDeserialier implements ObjectDeserialier {

    @Override
    public RpcRequestInstace deserialize(byte[] raw) throws SerializableException, RpcException {
        return null;
    }


}
