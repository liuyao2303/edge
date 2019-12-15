package org.liuyao.framework.rpc.deserialize;

import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;

public interface ObjectDeserialier<T> {
    T deserialize(byte[] raw) throws SerializableException, RpcException;
}
