package com.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyao.framework.lang.SerializableException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDeserializer implements ObjectDeserialier<String> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonDeserializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();


    public <A> A deserialize(String classType, String raw) throws SerializableException {
        if (StringUtils.isEmpty(classType)) {
            throw new SerializableException("classType null");
        }

        try {
            Class cls = Thread.currentThread().getContextClassLoader().loadClass(classType);
            return (A) objectMapper.readValue(String.valueOf(raw), cls);
        } catch (ClassNotFoundException e) {
            LOG.error("class not found for {}", classType);
            throw new SerializableException(e);
        } catch (JsonMappingException e) {
            LOG.error("JsonMappingException for", e);
            throw new SerializableException(e);
        } catch (JsonProcessingException e) {
            LOG.error("class not found for ", e);
            throw new SerializableException(e);
        }
    }
}
