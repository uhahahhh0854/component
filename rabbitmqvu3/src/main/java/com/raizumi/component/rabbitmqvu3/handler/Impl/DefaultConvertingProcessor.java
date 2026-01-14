package com.raizumi.component.rabbitmqvu3.handler.Impl;

import com.raizumi.component.common.handler.CompressHandlerDescriptor;
import com.raizumi.component.common.handler.DEHandleDescriptor;
import com.raizumi.component.common.handler.SEHandleDescriptor;
import com.raizumi.component.rabbitmqvu3.entity.Converter;
import com.raizumi.component.rabbitmqvu3.handler.meta.AbstractConvertingProcessor;

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
