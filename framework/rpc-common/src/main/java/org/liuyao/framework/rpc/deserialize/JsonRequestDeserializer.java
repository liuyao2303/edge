package org.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.lang.SerializableException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonRequestDeserializer implements ObjectDeserialier<RpcRequestInstace> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonRequestDeserializer.class);
    private static final String PARAM_TYPES = "paramTypes";
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String SERVICE_FIELD = "service";
    private static final String METHOD_FIELD = "method";
    private static final String RESULT_CLASS_FIELD = "resultClass";
    private static final String PARAMS_FIELDS = "params";

    @Override
    public RpcRequestInstace deserialize(byte[] raw) throws SerializableException, RpcException {

        try {
            Map<String,String> instanceParams = objectMapper
                    .readValue(raw, new TypeReference<Map<String, String>>() {});
            if (instanceParams == null || instanceParams.isEmpty()) {
                LOG.error("Rpc failed: param invalid {}", new String(raw));
                throw new RpcException("Rpc failed: param invalid");
            }
            
            return undecode(instanceParams);
        } catch (IOException e) {
            LOG.error("JsonMappingException for", e);
            throw new SerializableException(e);
        }
    }

    private RpcRequestInstace undecode(Map<String, String> instanceParams) throws RpcException {
        String service = instanceParams.get(SERVICE_FIELD);
        String method = instanceParams.get(METHOD_FIELD);
        String returnClass = instanceParams.get(RESULT_CLASS_FIELD);
        String params = instanceParams.get(PARAMS_FIELDS);
        String paraTypes = instanceParams.get(PARAM_TYPES);

        if (StringUtils.isEmpty(service) || StringUtils.isEmpty(params)) {
            LOG.error("Rpc failed: param invalid");
            throw new RpcException("Rpc failed: param invalid");
        }

        RpcRequestInstace instance = new RpcRequestInstace();
        instance.setServiceName(service);
        instance.setMethod(method);
        instance.setReturnClass(returnClass);
        Map<Integer, Object> paramObjects = Maps.newHashMap();
        try {
            Map<Integer, String> paramTypes = objectMapper
                    .readValue(paraTypes, new TypeReference<Map<Integer, String>>() {});
            if (paramTypes.isEmpty()) {
                //传递的是空的参数列表
                instance.setParams(paramObjects);
                return instance;
            }
            Class[] pCls = getParamTypes(paramTypes);

            Map<Integer, String> paramObjStr = objectMapper
                    .readValue(params, new TypeReference<Map<Integer, String>>() {});
            for (Integer index : paramObjStr.keySet()) {
                Class pc = pCls[index];
                Object obj = objectMapper.readValue(paramObjStr.get(index), pc);
                paramObjects.put(index, obj);
            }
            instance.setParams(paramObjects);
        } catch (ClassNotFoundException | JsonProcessingException e) {
            LOG.error("JsonMappingException for", e);
            throw new RpcException("Rpc failed: service not found" + service);
        }

        return instance;
    }

    private Class[] getParamTypes(Map<Integer, String> paraTypes) throws ClassNotFoundException {
        int count = paraTypes.size();
        Class[] classes = new Class[count];
        for (int i = 0; i < count; i++) {
            Class serviceClass = Thread.currentThread().getContextClassLoader().loadClass(paraTypes.get(i));
            classes[i] = serviceClass;
        }

        return classes;
    }
}
