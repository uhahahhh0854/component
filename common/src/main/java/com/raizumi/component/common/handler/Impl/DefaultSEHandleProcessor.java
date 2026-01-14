package com.raizumi.component.common.handler.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raizumi.component.common.enums.Mode;
import com.raizumi.component.common.handler.meta.AbstractSEHandleProcessor;

import java.nio.charset.Charset;

public class DefaultSEHandleProcessor extends AbstractSEHandleProcessor {

    public DefaultSEHandleProcessor() {
        super();
    }

    public DefaultSEHandleProcessor(Charset encoding) {
        super(encoding);
    }

    @Override
    public <T> byte[] serialize(T payload, Mode mode)  {
        try{
            if (mode == Mode.JSON){
                return new ObjectMapper().writeValueAsString(payload).getBytes(encoding);
            }
            if (mode == Mode.BYTES){
                return (byte[]) payload;
            }
            if (mode == Mode.PLAINTEXT){
                return new ObjectMapper().writeValueAsString(payload).getBytes(encoding);
            }

            return new ObjectMapper().writeValueAsBytes(payload);
        }catch (Exception e){
            throw new RuntimeException("Serialize exception in " + e.getMessage());
        }

    }

    @Override
    public Object deserialize(byte[] payload, Class<?> clazz, Mode mode) {

        if (mode == Mode.JSON && clazz == null){
            throw new RuntimeException("Deserialize in Mode JSON convert, but clazz is null");
        }

        try{
            if (mode == Mode.JSON){
                return new ObjectMapper().readValue(payload, clazz);
            }
            if (mode == Mode.PLAINTEXT){
                return new String(payload, encoding);
            }
            if (mode == Mode.BYTES){
                return payload;
            }

            return payload;
        }catch (Exception e){
            throw new RuntimeException("Deserialize exception in " + e.getMessage());
        }
    }

}
