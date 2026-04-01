package com.raizumi.component.rabbitmqvu3;

import com.raizumi.component.rabbitmqvu3.entity.Publisher;
import com.raizumi.component.rabbitmqvu3.handler.Impl.DefaultConvertingProcessor;
import com.raizumi.component.common.utils.ContextUtil;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.util.Optional;

import static com.raizumi.component.rabbitmqvu3.constants.ControlVar.MESSAGE_CONVERTER;

public class RabbitmqAdminister extends RabbitAdmin {
    private final ContextUtil contextUtil;

    RabbitmqAdminister(ContextUtil contextUtil, ConnectionFactory connectionFactory) {
        super(connectionFactory);
        this.contextUtil = contextUtil;
    }

    public Rabbitmqvu3Template buildTemplate(String beanName, Publisher publisher) {
        Rabbitmqvu3Template rabbitmqvu3Template = this.buildTemplate(publisher);

        contextUtil.register(beanName,Rabbitmqvu3Template.class,rabbitmqvu3Template);

        return rabbitmqvu3Template;
    }

    public Rabbitmqvu3Template buildTemplate(Publisher publisher) {
        Rabbitmqvu3Template template = new Rabbitmqvu3Template(contextUtil);

        if (!(publisher.getConnectionFactory() instanceof CachingConnectionFactory)) {
            throw new IllegalArgumentException("ConnectionFactory must be CachingConnectionFactory to use publisherConfirmType/publisherReturns");
        }else{
            CachingConnectionFactory cachingConnectionFactory = (CachingConnectionFactory) publisher.getConnectionFactory();

            cachingConnectionFactory.setPublisherConfirmType(
                    publisher.getPublisherConfirmType() != null
                            ? publisher.getPublisherConfirmType()
                            : CachingConnectionFactory.ConfirmType.NONE
            );
            cachingConnectionFactory.setPublisherReturns(publisher.isPublisherReturns());
            template.setConnectionFactory(cachingConnectionFactory);
        }

        template.setExchange(publisher.getDefaultExchange());
        template.setRoutingKey(publisher.getDefaultRoutingKey());
        template.setMandatory(publisher.isMandatory());
        template.setChannelTransacted(publisher.isChannelTransacted());

        if (publisher.getDefaultReceiveQueue() != null && !publisher.getDefaultReceiveQueue().isEmpty()) {
            template.setDefaultReceiveQueue(publisher.getDefaultReceiveQueue());
        }
        template.setReceiveTimeout(publisher.getReceiveTimeout());

        template.setReplyTimeout(publisher.getReplyTimeout());
        template.setUseTemporaryReplyQueues(publisher.isUseTemporaryReplyQueues());
        template.setUseDirectReplyToContainer(publisher.isUseDirectReplyToContainer());
        if (publisher.getReplyAddress() != null && !publisher.getReplyAddress().isEmpty()) {
            template.setReplyAddress(publisher.getReplyAddress());
        }
        template.setCorrelationKey(publisher.getCorrelationKey());

        // 加入自定义messageConverter
        if (publisher.getMessageConverter() != null) {
            template.setMessageConverter(publisher.getMessageConverter());
        }else if (publisher.getConverter() != null) {
            template.setMessageConverter(new DefaultConvertingProcessor(publisher.getConverter()));
        }else{
            Optional<MessageConverter> bean = contextUtil.getBean(MESSAGE_CONVERTER, MessageConverter.class);
            if (bean.isPresent()) {
                template.setMessageConverter(bean.get());
            }else{
                template.setMessageConverter(new SimpleMessageConverter());
            }
        }

        template.setConfirmCallback(publisher.getConfirmCallback());
        template.setReturnsCallback(publisher.getReturnsCallback());
        template.setMandatory(publisher.isTemplateMandatory());

        if (publisher.getBeforePublishPostProcessors() != null) {
            template.setBeforePublishPostProcessors(
                    publisher.getBeforePublishPostProcessors().toArray(new org.springframework.amqp.core.MessagePostProcessor[0])
            );
        }
        if (publisher.getAfterReceivePostProcessors() != null) {
            template.setAfterReceivePostProcessors(
                    publisher.getAfterReceivePostProcessors().toArray(new org.springframework.amqp.core.MessagePostProcessor[0])
            );
        }

        if (publisher.getRetryTemplate() != null) {
            template.setRetryTemplate(publisher.getRetryTemplate());
        }

        return template;
    }
}
