package com.liuyao.framework.rpc.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.*;

public class MessagePooledTask {

    private static final Logger LOG = LoggerFactory.getLogger(MessagePooledTask.class);
    private final BlockingDeque<RpcMessageRequest> msgQueue = new LinkedBlockingDeque<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(20, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread("MessagePooledTask");
        }
    });

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread("MessagePooledTask -- pooled Thread");
        }
    });

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

    public void start() {
        ScheduledExecutorService scheduledExecutorService
    }

    public void stop() {

    }

    public class MessagePoolTask implements Runnable {

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
}
