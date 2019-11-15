package com.liuyao.framework.rpc.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ObjectSerialier<T> {
    T serialize(Object obj) throws JsonProcessingException;
}
