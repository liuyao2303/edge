package com.liuyao.framework.rpc.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyao.framework.rpc.deserialize.JsonRpcRespFrame;

public class MessageEncoder {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonRpcRespFrame frame;


    public MessageEncoder(JsonRpcRespFrame frame) {
        this.frame = frame;
    }

    public byte[] decode() throws JsonProcessingException {
        String val = objectMapper.writeValueAsString(frame);
        return val.getBytes();
    }
}
