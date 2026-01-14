package com.raizumi.component.rabbitmqvu3.handler.meta;


import com.raizumi.component.rabbitmqvu3.entity.Converter;
import com.raizumi.component.rabbitmqvu3.handler.Impl.DefaultConvertingProcessor;
import com.raizumi.component.rabbitmqvu3.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BatchMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class MessageProcessor extends MessageListenerAdapter implements BatchMessageListener {
    private final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    public MessageProcessor(MessageHandler<?> messageHandler) {
        super(messageHandler, new SimpleMessageConverter());
    }

    public MessageProcessor(MessageHandler<?> messageHandler, MessageConverter messageConverter) {
        super(messageHandler, messageConverter);
    }

    public MessageProcessor(MessageHandler<?> messageHandler, Converter converter){
        super(messageHandler);
        DefaultConvertingProcessor defaultConvertingProcessor = new DefaultConvertingProcessor(converter);
        super.setMessageConverter(defaultConvertingProcessor);
    }

    @Override
    public void onMessage(Message message) {

        if (message == null){
            log.error("Message is null");
            return;
        }

        if (getMessageConverter() == null) throw new IllegalStateException("MessageConverter is not set");

        try {
            Object payload = getMessageConverter().fromMessage(message);
            invokeListenerMethod(getDefaultListenerMethod(), new Object[]{payload}, message);
        } catch (Exception e) {
            log.error("Error converting messages cause of ", e);
        }


    }

    @Override
    public void onMessageBatch(List<Message> messages) {
        if (messages == null || messages.isEmpty()) return;

        if (getMessageConverter() == null) throw new IllegalStateException("MessageConverter is not set");

        List<Object> payloads = new ArrayList<>(messages.size());
        for (Message message : messages) {
            try {
                Object payload = getMessageConverter().fromMessage(message); // 消息转换
                payloads.add(payload);
            } catch (Exception e) {
                log.error("Error converting messages cause of ", e);
            }
        }

        if (!payloads.isEmpty()) {
            String DEFAULT_HANDLE_MESSAGES = "handleMessages";
            invokeListenerMethod(DEFAULT_HANDLE_MESSAGES, new Object[]{payloads}, messages.get(0));
        }
    }
}
