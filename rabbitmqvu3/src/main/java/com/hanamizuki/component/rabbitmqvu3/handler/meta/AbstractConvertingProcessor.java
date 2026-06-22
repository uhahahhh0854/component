package com.hanamizuki.component.rabbitmqvu3.handler.meta;

import com.hanamizuki.component.common.handler.CompressHandlerDescriptor;
import com.hanamizuki.component.common.handler.DEHandleDescriptor;
import com.hanamizuki.component.common.handler.Impl.DefaultSEHandleProcessor;
import com.hanamizuki.component.common.handler.SEHandleDescriptor;
import com.hanamizuki.component.rabbitmqvu3.entity.Converter;
import com.hanamizuki.component.common.enums.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;


public abstract class AbstractConvertingProcessor extends AllowedListDeserializingMessageConverter
        implements BeanClassLoaderAware, MessageConverter {
    private final Logger log = LoggerFactory.getLogger(AbstractConvertingProcessor.class);

    private Class<?> clazz = Object.class;

    private Charset charset = StandardCharsets.UTF_8;

    private String DEType = "none";

    public DEHandleDescriptor deHandleDescriptor;

    private String SEType = "none";

    public SEHandleDescriptor seHandleDescriptor;

    private String CompressType = "none";

    public CompressHandlerDescriptor compressHandlerDescriptor;


    protected AbstractConvertingProcessor(Converter converter) {
        ppt(converter);
    }

    protected AbstractConvertingProcessor(Class<?> clazz,
                                          Charset charset,
                                          DEHandleDescriptor deHandleDescriptor,
                                          SEHandleDescriptor seHandleDescriptor,
                                          CompressHandlerDescriptor compressHandlerDescriptor) {

        Converter converter = new Converter();
        converter.setClazz(clazz);
        converter.setEncoding(charset.name());
        converter.setDeHandleDescriptor(deHandleDescriptor);
        converter.setSeHandleDescriptor(seHandleDescriptor);
        converter.setCompressHandlerDescriptor(compressHandlerDescriptor);
        this.ppt(converter);
    }

    protected AbstractConvertingProcessor(Class<?> clazz,
                                          Charset charset,
                                          DEHandleDescriptor deHandleDescriptor,
                                          SEHandleDescriptor seHandleDescriptor,
                                          String allowedListPattern) {

        Converter converter = new Converter();
        converter.setClazz(clazz);
        converter.setEncoding(charset.name());
        converter.setDeHandleDescriptor(deHandleDescriptor);
        converter.setSeHandleDescriptor(seHandleDescriptor);
        converter.setAllowedListPattern(allowedListPattern);
        this.ppt(converter);
    }

    private void ppt(Converter converter){
        converter.init();
        this.clazz = (converter.getClazz() != null) ? converter.getClazz() : Object.class;
        this.charset = (converter.getEncoding() != null ? Charset.forName(converter.getEncoding()) : StandardCharsets.UTF_8);
        if (converter.getDeHandleDescriptor() != null) {
            if (converter.getDeHandleDescriptor() instanceof DefaultSEHandleProcessor) {
                DEType = "default";
            }else{
                DEType = "user-defined";
            }

            this.deHandleDescriptor = converter.getDeHandleDescriptor();
        }else{
            this.deHandleDescriptor = new DEHandleDescriptor() {};
        }
        if (converter.getSeHandleDescriptor() != null) {
            if (converter.getSeHandleDescriptor() instanceof DefaultSEHandleProcessor) {
                SEType = "default";
            }else {
                SEType = "user-defined";
            }
            this.seHandleDescriptor = converter.getSeHandleDescriptor();
        }else{
            this.seHandleDescriptor = new DefaultSEHandleProcessor(StandardCharsets.UTF_8);
        }
        if (converter.getCompressHandlerDescriptor() != null) {
            if (converter.getCompressHandlerDescriptor() instanceof DefaultSEHandleProcessor) {
                CompressType = "default";
            }else{
                CompressType = "user-defined";
            }
            this.compressHandlerDescriptor = converter.getCompressHandlerDescriptor();
        }else {
            this.compressHandlerDescriptor = new CompressHandlerDescriptor() {};
        }
        if (converter.getAllowedListPattern() != null) {
            String[] split = converter.getAllowedListPattern().split(";");
            if (split.length > 0) {
                setAllowedListPatterns(Arrays.asList(split));
            }else {
                log.warn("Allowed list patterns is empty");
            }
        }
    }

    private Object inTransform(@NonNull byte[] resource, @NonNull Mode mode) {

        checkAllowedList(clazz);

        return seHandleDescriptor.deserialize(deHandleDescriptor.decrypt(resource),clazz, mode);
    }

    private byte[] outTransform(@NonNull Object in, @NonNull Mode mode) {

        if (mode == Mode.JAVA_JSON){
            checkAllowedList(in.getClass());
        }

        return deHandleDescriptor.encrypt(seHandleDescriptor.serialize(in,mode));
    }


    private String codebaseUrl;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Override
    public void setBeanClassLoader(
            @NonNull ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    /**
     * Converts from a AMQP Message to an Object.
     */
    @Override
    @NonNull
    public Object fromMessage(Message message) throws MessageConversionException {
        Object content = null;

        Mode mode = null;
        MessageProperties properties = message.getMessageProperties();

        if (properties != null) {
            Map<String, Object> headers = properties.getHeaders();
            if (!headers.containsKey("SEType") || !headers.containsKey("DEType") || !headers.containsKey("CompressType")) {
                throw new IllegalArgumentException("SEType or DEType not found");
            }

            if (!headers.get("SEType").equals(SEType)) {
                throw new IllegalArgumentException("SEType is not the same");
            }
            if (!headers.get("DEType").equals(DEType)) {
                throw new IllegalArgumentException("DEType is not the same");
            }
            if (!headers.get("CompressType").equals(CompressType)) {
                throw new IllegalArgumentException("CompressType is not the same");
            }

            String contentType = properties.getContentType();
            if (contentType != null) {
                mode = Mode.getMode(contentType);
            }
        }
        if (mode == null) {
            throw new IllegalArgumentException("Mode not found");
        }
        byte[] compress = null;
        try {
            compress = compressHandlerDescriptor.decompress(message.getBody());
        } catch (IOException e) {
            throw new MessageConversionException("Could not decompress message", e);
        }

        try{
            content = inTransform(compress, mode);
        }catch (Exception e){
            throw new MessageConversionException("Could not convert message. Headers="
                    + properties.getHeaders() + ", contentType=" + properties.getContentType(), e);
        }

        if (content == null) {
            content = message.getBody();
        }
        return content;
    }

    /**
     * Creates an AMQP Message from the provided Object.
     */
    @Override
    @NonNull
    protected Message createMessage(@NonNull Object object, @NonNull MessageProperties messageProperties) throws MessageConversionException {
        byte[] bytes = null;

        Mode mode = null;

        if (object instanceof byte[]) {
            mode = Mode.BYTES;
        }else if (object instanceof String) {
            mode = Mode.PLAINTEXT;
        }else if (object instanceof Serializable) {
            mode = Mode.JAVA_JSON;
        }else{
            mode = Mode.JSON;
        }

        try{
            bytes = outTransform(object, mode);
            bytes = compressHandlerDescriptor.compress(bytes);
        }catch (Exception e){
            throw new MessageConversionException("failed to create payload", e);
        }

        messageProperties.setContentType(mode.getContextType());
        messageProperties.setContentEncoding(charset.name());
        messageProperties.getHeaders().put("SEType", SEType);
        messageProperties.getHeaders().put("DEType", DEType);
        messageProperties.getHeaders().put("CompressType", CompressType);

        messageProperties.setContentLength(bytes.length);
        return new Message(bytes, messageProperties);
    }

    /**
     * Create an ObjectInputStream for the given InputStream and codebase. The default implementation creates a
     * CodebaseAwareObjectInputStream.
     * @param is the InputStream to read from
     * @param codebaseUrl the codebase URL to load classes from if not found locally (can be <code>null</code>)
     * @return the new ObjectInputStream instance to use
     * @throws IOException if creation of the ObjectInputStream failed
     * @see org.springframework.remoting.rmi.CodebaseAwareObjectInputStream
     */
    @SuppressWarnings("deprecation")
    protected ObjectInputStream createObjectInputStream(InputStream is, String codebaseUrl) throws IOException {
        return new org.springframework.remoting.rmi.CodebaseAwareObjectInputStream(is, this.beanClassLoader,
                codebaseUrl) {

            @NonNull
            @Override
            protected Class<?> resolveClass(@NonNull ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {
                Class<?> clazz = super.resolveClass(classDesc);
                checkAllowedList(clazz);
                return clazz;
            }
        };
    }
}
