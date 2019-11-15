package com.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuyao.framework.lang.SerializableException;

public interface ObjectDeserialier<T> {
    <A> A deserialize(String classType, T raw) throws  SerializableException;
}
