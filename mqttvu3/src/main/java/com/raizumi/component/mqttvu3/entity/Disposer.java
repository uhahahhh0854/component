package com.raizumi.component.mqttvu3.entity;

import com.raizumi.component.common.handler.Impl.AESDefaultDEHandleProcessor;
import com.raizumi.component.common.handler.Impl.DefaultSEHandleProcessor;
import com.raizumi.component.common.handler.SEHandleDescriptor;
import com.raizumi.component.mqttvu3.handler.*;
import com.raizumi.component.common.enums.Mode;
import com.raizumi.component.mqttvu3.handler.Impl.DefaultMessageAdviceCreator;
import com.raizumi.component.mqttvu3.handler.Impl.DefaultTransformingProcessor;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// advice异常订阅、序列化和加解密
public class Disposer implements Serializable {
    public Disposer() {
    }

    public void process(){
        this.partAdvice();
        this.partTF();
    }


    private MessageAdviceCreator messageAdviceCreator;

    /*  订阅异常处理*/
    private Vice vice = new Vice();

    public void partAdvice(){
        if (messageAdviceCreator == null) {
            messageAdviceCreator = new DefaultMessageAdviceCreator(vice);
        }
    }





    private TransformingDescriptor transformingDescriptor;

    //  反序列化后的类型，mode为JSON有效
    private Class<?> clazz = Object.class;

    public void partTF(){
        this.partDE();
        this.partSE();
        if (transformingDescriptor == null) {
            transformingDescriptor = new DefaultTransformingProcessor(clazz, deHandleDescriptor, seHandleDescriptor,
                    compressHandlerDescriptor == null ? new com.raizumi.component.common.handler.CompressHandlerDescriptor() {} : compressHandlerDescriptor);
        }
    }

    /*  入站处理*/
    private com.raizumi.component.common.handler.DEHandleDescriptor deHandleDescriptor;

    private String secretKey;

    private String algorithm;

    public void partDE(){
        if (deHandleDescriptor == null) {
            deHandleDescriptor = new AESDefaultDEHandleProcessor(secretKey,algorithm);
        }else{
            if (secretKey == null || secretKey.isEmpty() || algorithm == null || algorithm.trim().isEmpty()) {
                deHandleDescriptor = new com.raizumi.component.common.handler.DEHandleDescriptor(){};
            }
            deHandleDescriptor = new AESDefaultDEHandleProcessor(secretKey,algorithm);
        }
    }

    private SEHandleDescriptor seHandleDescriptor;
    //  订阅返回的类型
    private Mode mode = Mode.PLAINTEXT;

    private Charset encoding = StandardCharsets.UTF_8;

    public void partSE(){
        if (seHandleDescriptor == null) {
            seHandleDescriptor = new DefaultSEHandleProcessor(encoding);
        }
    }

    private com.raizumi.component.common.handler.CompressHandlerDescriptor compressHandlerDescriptor;

    public MessageAdviceCreator getMessageAdviceCreator() {
        return messageAdviceCreator;
    }

    public void setMessageAdviceCreator(MessageAdviceCreator messageAdviceCreator) {
        this.messageAdviceCreator = messageAdviceCreator;
    }

    public Vice getVice() {
        return vice;
    }

    public void setVice(Vice vice) {
        this.vice = vice;
    }

    public TransformingDescriptor getTransformingDescriptor() {
        return transformingDescriptor;
    }

    public void setTransformingDescriptor(TransformingDescriptor transformingDescriptor) {
        this.transformingDescriptor = transformingDescriptor;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public com.raizumi.component.common.handler.DEHandleDescriptor getDeHandleDescriptor() {
        return deHandleDescriptor;
    }

    public void setDeHandleDescriptor(com.raizumi.component.common.handler.DEHandleDescriptor deHandleDescriptor) {
        this.deHandleDescriptor = deHandleDescriptor;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public SEHandleDescriptor getSeHandleDescriptor() {
        return seHandleDescriptor;
    }

    public void setSeHandleDescriptor(SEHandleDescriptor seHandleDescriptor) {
        this.seHandleDescriptor = seHandleDescriptor;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public com.raizumi.component.common.handler.CompressHandlerDescriptor getCompressHandlerDescriptor() {
        return compressHandlerDescriptor;
    }

    public void setCompressHandlerDescriptor(com.raizumi.component.common.handler.CompressHandlerDescriptor compressHandlerDescriptor) {
        this.compressHandlerDescriptor = compressHandlerDescriptor;
    }
}
