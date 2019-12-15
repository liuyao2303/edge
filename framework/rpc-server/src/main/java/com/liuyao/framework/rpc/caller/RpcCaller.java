package com.liuyao.framework.rpc.caller;

import com.liuyao.framework.lang.ExecutionException;
import com.liuyao.framework.lang.RpcException;
import org.liuyao.framework.rpc.deserialize.RpcRequestMeta;
import com.liuyao.framework.service.meta.ServiceInfMeta;
import com.liuyao.framework.service.reg.ServiceRegister;
import com.liuyao.framework.service.reg.ServiceRegisterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RpcCaller {
    private static final Logger LOG = LoggerFactory.getLogger(RpcCaller.class);
    private RpcRequestMeta meta;

    public RpcCaller(RpcRequestMeta meta) {
        this.meta = meta;
    }

    public Optional call() throws RpcException {

        try {
            ServiceRegister serviceRegister = ServiceRegisterImpl.getInstance();
            ServiceInfMeta serviceMeta = serviceRegister.getServiceMeta(meta.getServiceClass());
            if (serviceMeta == null) {
                throw new RpcException(String.format("Rpc error: service for %s is not regeser", meta.getServiceClass()));
            }

            return serviceMeta.invoke(meta.getMethod(), meta.getParams());
        } catch (ExecutionException | RpcException e) {
            LOG.error("Rpc error: failed to execution rpc for messageID {}", meta.getMessageID());
            throw new RpcException(e.getMessage(), e);
        }
    }
}
