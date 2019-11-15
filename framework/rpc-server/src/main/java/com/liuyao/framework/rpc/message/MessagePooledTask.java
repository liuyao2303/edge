package com.liuyao.framework.rpc.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class MessagePooledTask implements Runnable {

    private static final MessagePooledTask INSTANCE = new MessagePooledTask();

    private static final Logger LOG = LoggerFactory.getLogger(MessagePooledTask.class);
    private final BlockingDeque<RpcMessageRequest> msgQueue = new LinkedBlockingDeque<>();
    private boolean isStart = false;


    private final static ExecutorService executor = Executors.newFixedThreadPool(20);

    public static MessagePooledTask getInstance() {
        if (!INSTANCE.isStart) {
            INSTANCE.start();
        }
        return INSTANCE;
    }

    private ExecutorService pollThreadPool = Executors.newFixedThreadPool(1);

    /**
     * 添加新的消息
     * @param request 消息载体
     */
    public void offer(RpcMessageRequest request) {
        if (request != null) {
            msgQueue.offer(request);
        }
    }

    /**
     *
     * @return 当前等待的任务数
     */
    public int waitMessages() {
        return msgQueue.size();
    }

    private void start() {
        pollThreadPool.submit(this);
        isStart = true;
    }

    public void stop() {

    }

    @Override
    public void run() {
        while (true) {
            try {
                RpcMessageRequest request = msgQueue.take();
                MessageDecoderTask task = new MessageDecoderTask(request);
                Future future = executor.submit(task);
            } catch (InterruptedException e) {
                LOG.error("occur in Message poll", e);
            }
        }
    }
}
