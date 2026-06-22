package com.hanamizuki.component.mqttvu3.entity;

import com.hanamizuki.component.common.handler.CompressHandlerDescriptor;
import com.hanamizuki.component.common.handler.DEHandleDescriptor;
import com.hanamizuki.component.mqttvu3.handler.MessageAdviceCreator;
import com.hanamizuki.component.mqttvu3.handler.TransformingDescriptor;
import com.hanamizuki.component.common.handler.Impl.AESDefaultDEHandleProcessor;
import com.hanamizuki.component.common.handler.Impl.DefaultSEHandleProcessor;
import com.hanamizuki.component.common.handler.SEHandleDescriptor;
import com.hanamizuki.component.mqttvu3.handler.*;
import com.hanamizuki.component.common.enums.Mode;
import com.hanamizuki.component.mqttvu3.handler.Impl.DefaultMessageAdviceCreator;
import com.hanamizuki.component.mqttvu3.handler.Impl.DefaultTransformingProcessor;

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
                    compressHandlerDescriptor == null ? new CompressHandlerDescriptor() {} : compressHandlerDescriptor);
        }
    }

    /*  入站处理*/
    private DEHandleDescriptor deHandleDescriptor;

    private String secretKey;

    private String algorithm;

    public void partDE(){
        if (deHandleDescriptor == null) {
            deHandleDescriptor = new AESDefaultDEHandleProcessor(secretKey,algorithm);
        }else{
            if (secretKey == null || secretKey.isEmpty() || algorithm == null || algorithm.trim().isEmpty()) {
                deHandleDescriptor = new DEHandleDescriptor(){};
            }
            deHandleDescriptor = new AESDefaultDEHandleProcessor(secretKey,algorithm);
        }
    }

    private SEHandleDescriptor seHandleDescriptor;
    //  订阅返回的类型
    private Mode mode = Mode.JSON;

    private Charset encoding = StandardCharsets.UTF_8;

    public void partSE(){
        if (seHandleDescriptor == null) {
            seHandleDescriptor = new DefaultSEHandleProcessor(encoding);
        }
    }

    private CompressHandlerDescriptor compressHandlerDescriptor;

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

    public DEHandleDescriptor getDeHandleDescriptor() {
        return deHandleDescriptor;
    }

    public void setDeHandleDescriptor(DEHandleDescriptor deHandleDescriptor) {
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

    public CompressHandlerDescriptor getCompressHandlerDescriptor() {
        return compressHandlerDescriptor;
    }

    public void setCompressHandlerDescriptor(CompressHandlerDescriptor compressHandlerDescriptor) {
        this.compressHandlerDescriptor = compressHandlerDescriptor;
    }

    public String toString(){
        return super.toString() +
                " deHandleDescriptor: " +
                deHandleDescriptor +
                ", secretKey: " +
                secretKey +
                ", algorithm: " +
                algorithm +
                ", mode: " +
                mode +
                ", encoding: " +
                encoding +
                ", compressHandlerDescriptor: " +
                compressHandlerDescriptor +
                ", messageAdviceCreator: " +
                messageAdviceCreator;
    }
}
