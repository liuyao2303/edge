package com.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuyao.framework.lang.SerializableException;

public interface ObjectDeserialier {
    <A> A deserialize(Class<A> classType, byte[] raw) throws  SerializableException;
}
