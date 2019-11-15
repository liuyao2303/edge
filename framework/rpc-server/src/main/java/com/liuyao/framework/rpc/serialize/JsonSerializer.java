package com.liuyao.framework.rpc.serialize;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json 序列号工具， 序列成json字符串
 */
public class JsonSerializer implements ObjectSerialier<String> {

    ObjectMapper objectMapper = new ObjectMapper();

    public String serialize(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
