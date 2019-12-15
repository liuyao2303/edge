package com.liuyao.framework.rpc.client.handler;

import com.liuyao.framework.rpc.client.caller.MessageProcessRegister;
import com.liuyao.framework.rpc.client.req.MessageProcess;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.liuyao.framework.rpc.req.RpcMessage;

public class RpcMessageInHandler extends ChannelInboundHandlerAdapter {

    private MessageProcessRegister processRegister = MessageProcessRegister.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            String msgId = rpcMessage.getMessageId();
            if (StringUtils.isEmpty(msgId)) {
                //对于没有messageId的请求，丢弃
                return;
            }
            MessageProcess process = processRegister.getMessageProcess(rpcMessage.getMessageId());
            if (process == null) {
                //丢弃以及不用的message Process
                return;
            }
            //调用相应的处理程序
            process.whenComplete(rpcMessage);
            return;
        }
        super.channelRead(ctx, msg);
    }
}
