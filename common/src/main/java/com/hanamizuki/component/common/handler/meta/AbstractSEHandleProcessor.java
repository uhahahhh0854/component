package com.hanamizuki.component.common.handler.meta;

import com.hanamizuki.component.common.enums.Mode;
import com.hanamizuki.component.common.handler.SEHandleDescriptor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractSEHandleProcessor implements SEHandleDescriptor {

    public Charset encoding = StandardCharsets.UTF_8;

    public AbstractSEHandleProcessor() {
    }

    public AbstractSEHandleProcessor(Charset encoding) {
        this.encoding = encoding;
    }

    public abstract  <T> byte[] serialize(T payload, Mode mode);


    public abstract Object deserialize(byte[] payload, Class<?> clazz, Mode mode);
}
