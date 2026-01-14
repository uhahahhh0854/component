package com.raizumi.component.common.handler;

import com.raizumi.component.common.enums.Mode;

/*  默认用 Jackson*/
public interface SEHandleDescriptor {
    <T> byte[] serialize(T payload, Mode mode);

    Object deserialize(byte[] payload, Class<?> clazz, Mode mode);
}
