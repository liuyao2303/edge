package com.liuyao.framework.rpc.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuyao.framework.lang.SerializableException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonDeserializer implements ObjectDeserialier {

    private static final Logger LOG = LoggerFactory.getLogger(JsonDeserializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();


    public <A> A deserialize(Class<A> classType, byte[] raw) throws SerializableException {
        try {
            return objectMapper.readValue(raw, classType);
        } catch (JsonMappingException e) {
            LOG.error("JsonMappingException for", e);
            throw new SerializableException(e);
        } catch (JsonProcessingException e) {
            LOG.error("class not found for ", e);
            throw new SerializableException(e);
        } catch (IOException e) {
            LOG.error("class not found for ", e);
            throw new SerializableException(e);
        }
    }
}
