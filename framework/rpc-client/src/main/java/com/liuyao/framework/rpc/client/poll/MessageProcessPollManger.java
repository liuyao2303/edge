package com.liuyao.framework.rpc.client.poll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageProcessPollManger implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessPollManger.class);
    private static final MessageProcessPollManger manager = new MessageProcessPollManger();
    private ScheduledExecutorService scheduledExecutorService
            = Executors.newScheduledThreadPool(5);
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public MessageProcessPollManger() {
        scheduledExecutorService.schedule(this, 10, TimeUnit.SECONDS);
    }

    public static MessageProcessPollManger getInstance() {
        return manager;
    }

    @Override
    public void run() {
        LOG.info("check Message pool status start");
        MessageProcessPollerTask task = MessageProcessPollerTask.getInstance();
        if (!task.getStart().get()) {
            LOG.info("start MessageProcessPollerTask");
            executorService.submit(task);
        }
        LOG.info("check Message pool status end");
    }
}
