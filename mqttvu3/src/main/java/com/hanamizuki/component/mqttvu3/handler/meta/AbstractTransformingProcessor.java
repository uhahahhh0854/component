package com.hanamizuki.component.mqttvu3.handler.meta;

import com.hanamizuki.component.common.enums.Mode;
import com.hanamizuki.component.common.handler.Impl.DefaultSEHandleProcessor;
import com.hanamizuki.component.mqttvu3.exception.PahoException;
import com.hanamizuki.component.common.handler.CompressHandlerDescriptor;
import com.hanamizuki.component.common.handler.DEHandleDescriptor;
import com.hanamizuki.component.common.handler.SEHandleDescriptor;
import com.hanamizuki.component.mqttvu3.handler.TransformingDescriptor;
import org.springframework.integration.transformer.GenericTransformer;

import java.io.IOException;

public abstract class AbstractTransformingProcessor implements TransformingDescriptor {
    private Class<?> clazz;

    public DEHandleDescriptor deHandleDescriptor;

    public SEHandleDescriptor seHandleDescriptor;

    public CompressHandlerDescriptor compressHandlerDescriptor;

    public AbstractTransformingProcessor(){
        clazz = null;
        deHandleDescriptor = new DEHandleDescriptor() {};

        seHandleDescriptor = new SEHandleDescriptor() {

            @Override
            public <T> byte[] serialize(T payload, Mode mode) {
                return new byte[0];
            }

            @Override
            public Object deserialize(byte[] payload, Class<?> clazz, Mode mode) {
                return null;
            }
        };
    }

    public AbstractTransformingProcessor(Class<?> clazz,
                                         DEHandleDescriptor deHandleDescriptor,
                                         SEHandleDescriptor seHandleDescriptor,
                                         CompressHandlerDescriptor compressHandlerDescriptor) {
        if (clazz != null) {
            this.clazz = clazz;
        }
        if (deHandleDescriptor != null) {
            this.deHandleDescriptor = deHandleDescriptor;
        }else{
            this.deHandleDescriptor = new DEHandleDescriptor() {};
        }
        if (seHandleDescriptor != null) {
            this.seHandleDescriptor = seHandleDescriptor;
        }else{
            this.seHandleDescriptor = new DefaultSEHandleProcessor();
        }
        if (compressHandlerDescriptor != null) {
            this.compressHandlerDescriptor = compressHandlerDescriptor;
        }else{
            this.compressHandlerDescriptor = new CompressHandlerDescriptor() {};
        }
    }


    public Object inTransformImpl(byte[] source, Mode mode) {
        try{
            if(compressHandlerDescriptor != null){
                source = compressHandlerDescriptor.decompress(source);
            }
        } catch (IOException e) {
            throw new PahoException(PahoException.Type.ELSE,"Failed to decompress payload");
        }
        return seHandleDescriptor.deserialize(deHandleDescriptor.decrypt(source),clazz, mode);
    }

    public byte[] outTransformImpl(Object source, Mode mode) {
        byte[] encrypt = deHandleDescriptor.encrypt(seHandleDescriptor.serialize(source, mode));
        try{
            if (compressHandlerDescriptor != null) {
                encrypt = compressHandlerDescriptor.compress(encrypt);
            }
        }catch (IOException e){
            throw new PahoException(PahoException.Type.ELSE,"Failed to decompress payload");
        }

        return encrypt;
    }

    @Override
    public GenericTransformer<byte[], Object> inTransform(Mode mode){
        return payload -> this.inTransformImpl(payload,mode);
    }

    @Override
    public GenericTransformer<Object, byte[]> outTransform(Mode mode){
        return  payload -> this.outTransformImpl(payload,mode);
    }
}
