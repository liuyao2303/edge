package org.liuyao.framework.rpc.type;

public enum RpcType {
    JSON(1),
    HEASON(2),
    PROTOBUF(3);

    private int value;

    RpcType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RpcType from(int type) {
        if (type == 1) {
            return JSON;
        }

        if (type == 2) {
            return HEASON;
        }

        return null;
    }
}
