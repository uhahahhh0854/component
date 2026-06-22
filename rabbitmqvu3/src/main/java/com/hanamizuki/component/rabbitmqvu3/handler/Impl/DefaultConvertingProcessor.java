package com.hanamizuki.component.rabbitmqvu3.handler.Impl;

import com.hanamizuki.component.common.handler.CompressHandlerDescriptor;
import com.hanamizuki.component.common.handler.DEHandleDescriptor;
import com.hanamizuki.component.common.handler.SEHandleDescriptor;
import com.hanamizuki.component.rabbitmqvu3.entity.Converter;
import com.hanamizuki.component.rabbitmqvu3.handler.meta.AbstractConvertingProcessor;

import java.nio.charset.Charset;


public class DefaultConvertingProcessor extends AbstractConvertingProcessor {

    public DefaultConvertingProcessor(Converter converter) {
        super(converter);
    }

    public DefaultConvertingProcessor(Class<?> clazz,
                                      Charset charset,
                                      DEHandleDescriptor deHandleDescriptor,
                                      SEHandleDescriptor seHandleDescriptor,
                                      CompressHandlerDescriptor compressHandlerDescriptor) {
        super(clazz, charset, deHandleDescriptor, seHandleDescriptor, compressHandlerDescriptor);
    }

}
