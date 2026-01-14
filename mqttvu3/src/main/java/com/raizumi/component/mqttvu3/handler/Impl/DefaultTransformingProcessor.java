package com.raizumi.component.mqttvu3.handler.Impl;

import com.raizumi.component.common.handler.CompressHandlerDescriptor;
import com.raizumi.component.common.handler.DEHandleDescriptor;
import com.raizumi.component.common.handler.SEHandleDescriptor;
import com.raizumi.component.mqttvu3.handler.meta.AbstractTransformingProcessor;

public class DefaultTransformingProcessor extends AbstractTransformingProcessor {

    public DefaultTransformingProcessor(){
        super();
    }

    public DefaultTransformingProcessor(Class<?> clazz,
                                        DEHandleDescriptor deHandleDescriptor,
                                        SEHandleDescriptor seHandleDescriptor,
                                        CompressHandlerDescriptor compressHandlerDescriptor) {
        super(clazz, deHandleDescriptor, seHandleDescriptor, compressHandlerDescriptor);
    }

}
