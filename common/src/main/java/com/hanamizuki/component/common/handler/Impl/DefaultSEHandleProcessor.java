package com.hanamizuki.component.common.handler.Impl;

import com.google.gson.Gson;
import com.hanamizuki.component.common.enums.Mode;
import com.hanamizuki.component.common.handler.meta.AbstractSEHandleProcessor;

import java.nio.charset.Charset;

public class DefaultSEHandleProcessor extends AbstractSEHandleProcessor {

    private static final Gson gson = new Gson();

    public DefaultSEHandleProcessor() {
        super();
    }

    public DefaultSEHandleProcessor(Charset encoding) {
        super(encoding);
    }

    @Override
    public <T> byte[] serialize(T payload, Mode mode)  {
        try{
            if (mode == Mode.JSON || mode == Mode.JAVA_JSON){
                return gson.toJson(payload).getBytes(encoding);
            }
            if (mode == Mode.BYTES){
                return (byte[]) payload;
            }
            if (mode == Mode.PLAINTEXT){
                return String.valueOf(payload).getBytes(encoding);
            }

            return gson.toJson(payload).getBytes(encoding);
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
            if (mode == Mode.JSON || mode == Mode.JAVA_JSON){
                return gson.fromJson(new String(payload, encoding), clazz);
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
