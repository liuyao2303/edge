package com.liuyao.framework.rpc.client.poll;

import com.liuyao.framework.lang.AppException;
import com.liuyao.framework.rpc.client.caller.MessageProcessRegister;
import com.liuyao.framework.rpc.client.req.BlockingRpcMessageProcess;
import com.liuyao.framework.rpc.client.req.MessageProcess;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageProcessPollerTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessPollerTask.class);
    private static final MessageProcessPollerTask INSTANCE = new MessageProcessPollerTask();

    public static MessageProcessPollerTask getInstance() {
        return INSTANCE;
    }

    private AtomicBoolean start = new AtomicBoolean(false);

    private MessageProcessPollerTask() {

    }

    @Override
    public void run() {
        start.set(true);
        while (true) {
            try {
                MessageProcessRegister register = MessageProcessRegister.getInstance();
                List<MessageProcess> messageProcesses = register.getAll();
                for (MessageProcess messageProcess : messageProcesses) {
                    checkMessageProcess(messageProcess);
                }
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                LOG.error("err occur in MessageProcessPollerTask");
                start.set(false);
                return;
            }
        }
    }

    private void checkMessageProcess(MessageProcess messageProcess) {
        Channel channel = ((BlockingRpcMessageProcess) messageProcess).getCaller().getChannel();
        if (channel == null || !channel.isRegistered() || !channel.isActive() || !channel.isWritable()) {
            messageProcess.whenRpcError(((BlockingRpcMessageProcess) messageProcess).getCaller().getRequest().getMessageId(),
                    new AppException("Channel inactive"));
        }
    }

    public AtomicBoolean getStart() {
        return start;
    }
}
