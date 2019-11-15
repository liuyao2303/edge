package com.liuyao.framework.rpc.message;

public class MessageDecoderTask implements Runnable {

    private RpcMessageRequest request;

    public MessageDecoderTask(RpcMessageRequest request) {
        this.request = request;
    }

    @Override
    public void run() {

    }
}
