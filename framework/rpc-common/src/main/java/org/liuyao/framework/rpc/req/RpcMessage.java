package org.liuyao.framework.rpc.req;

public class RpcMessage {
    private long header;

    private String messageId;

    private int rpcType;

    private int length;

    private byte[] data;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getHeader() {
        return header;
    }

    public void setHeader(long header) {
        this.header = header;
    }

    public int getRpcType() {
        return rpcType;
    }

    public void setRpcType(int rpcType) {
        this.rpcType = rpcType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcMessage{" +
                "header=" + header +
                ", rpcType=" + rpcType +
                ", length=" + length +
                ", data=" + new String(data) +
                '}';
    }
}
