package com.raizumi.component.rabbitmqvu3.entity;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

/**
 * RabbitTemplate 配置实体类（仅模板相关参数）
 *
 * 功能：
 * 1. 消息转换器：MessageConverter、消息发送前/接收后处理器
 * 2. 发布确认/Return 回调：ConfirmCallback、ReturnsCallback、mandatory
 * 3. 默认发送/接收目标：exchange、routingKey、默认接收队列
 * 4. 阻塞超时：receiveTimeout、replyTimeout
 * 5. 消息发送增强：事务通道、重试策略
 * 6. 可选扩展：CorrelationData 自定义处理器
 */
public class Template {

    private MessageConverter messageConverter;
    private MessagePostProcessor beforePublishPostProcessor;
    private List<MessagePostProcessor> afterReceivePostProcessors;

    private RabbitTemplate.ConfirmCallback confirmCallback;
    private RabbitTemplate.ReturnsCallback returnsCallback;
    private List<RabbitTemplate.ReturnsCallback> returnsCallbacks;
    private boolean mandatory = false;

    //  rabbitTemplate 调用相关方法的默认参数
    private String exchange;
    private String routingKey;
    private String defaultReceiveQueue;
    private long receiveTimeout = 0L;
    private long replyTimeout = 0L;

    private boolean channelTransacted = false;
    //  rabbitmqTemplate 的重试策略策略
    private RetryTemplate retryTemplate;

    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public MessagePostProcessor getBeforePublishPostProcessor() {
        return beforePublishPostProcessor;
    }

    public void setBeforePublishPostProcessor(MessagePostProcessor beforePublishPostProcessor) {
        this.beforePublishPostProcessor = beforePublishPostProcessor;
    }

    public List<MessagePostProcessor> getAfterReceivePostProcessors() {
        return afterReceivePostProcessors;
    }

    public void setAfterReceivePostProcessors(List<MessagePostProcessor> afterReceivePostProcessors) {
        this.afterReceivePostProcessors = afterReceivePostProcessors;
    }

    public RabbitTemplate.ConfirmCallback getConfirmCallback() {
        return confirmCallback;
    }

    public void setConfirmCallback(RabbitTemplate.ConfirmCallback confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public RabbitTemplate.ReturnsCallback getReturnsCallback() {
        return returnsCallback;
    }

    public void setReturnsCallback(RabbitTemplate.ReturnsCallback returnsCallback) {
        this.returnsCallback = returnsCallback;
    }

    public List<RabbitTemplate.ReturnsCallback> getReturnsCallbacks() {
        return returnsCallbacks;
    }

    public void setReturnsCallbacks(List<RabbitTemplate.ReturnsCallback> returnsCallbacks) {
        this.returnsCallbacks = returnsCallbacks;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getDefaultReceiveQueue() {
        return defaultReceiveQueue;
    }

    public void setDefaultReceiveQueue(String defaultReceiveQueue) {
        this.defaultReceiveQueue = defaultReceiveQueue;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public long getReplyTimeout() {
        return replyTimeout;
    }

    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }

    public boolean isChannelTransacted() {
        return channelTransacted;
    }

    public void setChannelTransacted(boolean channelTransacted) {
        this.channelTransacted = channelTransacted;
    }

    public RetryTemplate getRetryTemplate() {
        return retryTemplate;
    }

    public void setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }
}

