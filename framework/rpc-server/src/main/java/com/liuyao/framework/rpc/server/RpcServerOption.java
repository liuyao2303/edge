package com.liuyao.framework.rpc.server;

import java.util.HashMap;
import java.util.Map;

public class RpcServerOption {

    public static final String SERIALIZER_PROVIDER_KEY = "rpc.serializer.provider";
    public static final String SERIALIZER_PROVIDER_JACKSON = "rpc.serializer.provider.jackson";

    private static Map<String, String> registers = new HashMap<>();

    public Map<String, String> getRegisters() {
        return registers;
    }

    public static void register(String key, String option) {
        registers.put(key, option);
    }

    public void copyFrom(RpcServerOption option) {
        if (option != null)
            registers.putAll(option.getRegisters());
    }

}
