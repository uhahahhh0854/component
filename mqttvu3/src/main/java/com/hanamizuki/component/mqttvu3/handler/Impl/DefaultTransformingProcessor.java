package com.hanamizuki.component.mqttvu3.handler.Impl;

import com.hanamizuki.component.common.handler.CompressHandlerDescriptor;
import com.hanamizuki.component.common.handler.DEHandleDescriptor;
import com.hanamizuki.component.common.handler.SEHandleDescriptor;
import com.hanamizuki.component.mqttvu3.handler.meta.AbstractTransformingProcessor;

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
