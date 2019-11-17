package com.liuyao.framework.rpc.server.message;

public class RpcRespMessage {

    private String messageId;

    private int rpcType;

    private byte[] rawBytes;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getRpcType() {
        return rpcType;
    }

    public void setRpcType(int rpcType) {
        this.rpcType = rpcType;
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }

    public void setRawBytes(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }
}
