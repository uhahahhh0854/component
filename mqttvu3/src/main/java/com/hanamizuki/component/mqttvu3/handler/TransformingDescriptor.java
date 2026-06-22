package com.hanamizuki.component.mqttvu3.handler;

import com.hanamizuki.component.common.enums.Mode;
import org.springframework.integration.transformer.GenericTransformer;

public interface TransformingDescriptor {
    GenericTransformer<byte[], Object> inTransform(Mode mode);

    GenericTransformer<Object, byte[]> outTransform(Mode mode);
}
