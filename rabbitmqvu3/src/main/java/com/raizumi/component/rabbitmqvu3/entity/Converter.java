package com.raizumi.component.rabbitmqvu3.entity;

import com.raizumi.component.common.handler.CompressHandlerDescriptor;
import com.raizumi.component.common.handler.DEHandleDescriptor;
import com.raizumi.component.common.handler.Impl.AESDefaultDEHandleProcessor;
import com.raizumi.component.common.handler.Impl.DefaultSEHandleProcessor;
import com.raizumi.component.common.handler.SEHandleDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 默认内置参数格式如下，现在加密参数只支持aes和默认jackson系列化
 *  spring:
 *      rabbitmq:
 *          converter:
 *              clazz:
 *              secretKey:
 *              algorithm:
 *              encoding:
 */
@ConfigurationProperties(
        prefix = "spring.rabbitmq.converter"
)
public class Converter {

    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    public Converter() {
    }

    public Converter(Class<?> clazz, DEHandleDescriptor deHandleDescriptor, String secretKey, String algorithm, SEHandleDescriptor seHandleDescriptor, String encoding, CompressHandlerDescriptor compressHandlerDescriptor, String allowedListPattern) {
        this.clazz = clazz;
        this.deHandleDescriptor = deHandleDescriptor;
        this.secretKey = secretKey;
        this.algorithm = algorithm;
        this.seHandleDescriptor = seHandleDescriptor;
        this.encoding = encoding;
        this.compressHandlerDescriptor = compressHandlerDescriptor;
        this.allowedListPattern = allowedListPattern;
    }

    private Class<?> clazz = Object.class;

    public void init() {
        if (secretKey == null || secretKey.isEmpty() || algorithm == null || algorithm.trim().isEmpty()) {
            deHandleDescriptor = new DEHandleDescriptor(){};
        }
        deHandleDescriptor = new AESDefaultDEHandleProcessor(secretKey,algorithm);
        if (seHandleDescriptor == null) {
            Charset charset = StandardCharsets.UTF_8;
            try{
                charset = Charset.forName(encoding);
            }catch (Exception e){
                log.info("charset's type is nor available and set in default");
            }

            seHandleDescriptor = new DefaultSEHandleProcessor(charset);
        }
        if (compressHandlerDescriptor == null) {
            compressHandlerDescriptor = new CompressHandlerDescriptor() {};
        }
    }


    /*  解密处理*/
    private DEHandleDescriptor deHandleDescriptor;

    private String secretKey;

    private String algorithm;



    /*  序列化处理*/
    private SEHandleDescriptor seHandleDescriptor;

    private String encoding = "UTF-8";


    /*  压缩*/
    private CompressHandlerDescriptor compressHandlerDescriptor;

    /**
     * 可允许反序列化类
     */
    private String allowedListPattern;

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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public CompressHandlerDescriptor getCompressHandlerDescriptor() {
        return compressHandlerDescriptor;
    }

    public void setCompressHandlerDescriptor(CompressHandlerDescriptor compressHandlerDescriptor) {
        this.compressHandlerDescriptor = compressHandlerDescriptor;
    }

    public String getAllowedListPattern() {
        return allowedListPattern;
    }

    public void setAllowedListPattern(String allowedListPattern) {
        this.allowedListPattern = allowedListPattern;
    }
}