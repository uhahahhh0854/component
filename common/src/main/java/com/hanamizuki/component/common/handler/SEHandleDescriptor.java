package com.hanamizuki.component.common.handler;

import com.hanamizuki.component.common.enums.Mode;

/*  默认用 Jackson*/
public interface SEHandleDescriptor {
    <T> byte[] serialize(T payload, Mode mode);

    Object deserialize(byte[] payload, Class<?> clazz, Mode mode);
}
