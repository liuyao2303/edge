package org.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonReponseDeserializer implements ObjectDeserialier<RpcResponseInstance> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonReponseDeserializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_MSG = "errorMsg";
    private static final String SERVICE_NAME = "service";
    private static final String SERVICE_METHOD = "method";
    private static final String RESULT_CLASS = "resultClass";
    private static final String RESULT = "result";

    @Override
    public RpcResponseInstance deserialize(byte[] raw) throws SerializableException, RpcException {

        try {
            Map<String,String> instanceParams = objectMapper
                    .readValue(raw, new TypeReference<Map<String, String>>() {});
            if (instanceParams == null || instanceParams.isEmpty()) {
                LOG.error("Rpc failed: param invalid {}", new String(raw));
                throw new RpcException("Rpc failed: rpc result failed");
            }
            
            return undecode(instanceParams);
        } catch (IOException e) {
            LOG.error("JsonMappingException for", e);
            throw new SerializableException(e);
        }
    }

    private RpcResponseInstance undecode(Map<String, String> instanceParams) throws RpcException {
        String service = instanceParams.get(SERVICE_NAME);
        String serviceMethod = instanceParams.get(SERVICE_METHOD);
        String result = instanceParams.get(RESULT);
        String rt = instanceParams.get(RESULT_CLASS);
        String errorCode = instanceParams.get(ERROR_CODE);
        String errorMsg = instanceParams.get(ERROR_MSG);

        RpcResponseInstance instance = new RpcResponseInstance();
        instance.setErrorCode(Integer.parseInt(errorCode));
        instance.setErrorMsg(errorMsg);
        instance.setServiceName(service);
        instance.setMethod(serviceMethod);

        try {
            Class rtClass = Thread.currentThread().getContextClassLoader().loadClass(rt);
            Object obj = objectMapper.readValue(result, rtClass);
            instance.setResult(obj);
        } catch (ClassNotFoundException e) {
            LOG.error("Rpc failed: param invalid {}", rt);
            throw new RpcException("Rpc failed: rpc result failed");
        } catch (JsonMappingException e) {
            LOG.error("Rpc failed: param invalid {}", rt);
            throw new RpcException("Rpc failed: rpc result failed");
        } catch (JsonProcessingException e) {
            LOG.error("Rpc failed: param invalid {}", rt);
            throw new RpcException("Rpc failed: rpc result failed");
        }

        return instance;
    }
}
