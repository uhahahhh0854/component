package com.raizumi.component.rabbitmqvu3.entity;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

/**
 * RabbitTemplate 配置参数实体类（最终整理版）
 *
 * 参数说明：
 *
 * 1. 连接与发送相关
 * - connectionFactory           : RabbitMQ 连接工厂，默认从 IOC 容器获取（必填）
 * - defaultExchange             : 默认交换机
 * - defaultRoutingKey           : 默认路由键
 * - mandatory                   : template 级别 mandatory（是否触发 Return 回调）
 * - templateMandatory           : 是否启用 RabbitTemplate 自身 mandatory 机制（可选）
 * - messageDeliveryMode         : 消息投递模式（持久化 / 非持久化）
 * - channelTransacted           : 是否启用 Channel 事务
 * <p>
 * 2. 接收/拉取相关
 * - defaultReceiveQueue         : 默认接收队列，用于 receive() 系列方法
 * - receiveTimeout              : receive() 超时时间（毫秒）
 * <p>
 * 3. RPC/回复相关
 * - replyTimeout                : RPC 模式下的超时时间（毫秒）
 * - useTemporaryReplyQueues     : RPC 模式是否使用临时 reply 队列
 * - useDirectReplyToContainer   : RPC 模式是否使用 direct-reply-to（性能更好）
 * - replyAddress                : RPC 固定 replyTo 地址（可选）
 * - correlationKey              : RPC correlationId 使用的字段名
 * <p>
 * 4. 消息处理器与转换器
 * - messageConverter            : 消息转换器（如 Jackson2JsonMessageConverter）
 * - encoding                    : 字符编码，默认 UTF-8
 * - beforePublishPostProcessors : 消息发送前处理器列表
 * - afterReceivePostProcessors  : 消息接收后处理器列表
 * <p>
 * 5. 发布确认与返回回调
 * - publisherConfirmType        : 发布确认模式（NONE / CORRELATED / SIMPLE）
 * - publisherReturns            : 是否启用发布返回
 * - confirmCallback             : 发布确认回调
 * - returnsCallback             : 发布返回回调
 * <p>
 * 6. 重试与恢复策略
 * - retryTemplate               : 消息发送失败时的重试策略
 * - recoveryCallback            : 重试失败后的恢复回调
 */
public class Publisher {

    private ConnectionFactory connectionFactory;

    private String defaultExchange = "defaultExchange";

    private String defaultRoutingKey = "defaultRoutingKey";

    private boolean mandatory = true;

    private boolean templateMandatory = false;

    private MessageDeliveryMode messageDeliveryMode = MessageDeliveryMode.PERSISTENT;

    private boolean channelTransacted = false;


    private String defaultReceiveQueue = "defaultQueue";

    private long receiveTimeout = 0L;


    private long replyTimeout = 5000L;

    private boolean useTemporaryReplyQueues = false;

    private boolean useDirectReplyToContainer = true;

    private String replyAddress;

    private String correlationKey = "correlationId";


    private MessageConverter messageConverter;

    private Converter converter;

    private String encoding = "UTF-8";

    private List<MessagePostProcessor> beforePublishPostProcessors;

    private List<MessagePostProcessor> afterReceivePostProcessors;


    private CachingConnectionFactory.ConfirmType publisherConfirmType = CachingConnectionFactory.ConfirmType.NONE;

    private boolean publisherReturns = false;

    private RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {

    };

    private RabbitTemplate.ReturnsCallback returnsCallback = new RabbitTemplate.ReturnsCallback() {

        @Override
        public void returnedMessage(@NonNull ReturnedMessage returned) {

        }
    };


    private RetryTemplate retryTemplate;

    private RecoveryCallback<?> recoveryCallback;

    public  Publisher(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getDefaultExchange() {
        return defaultExchange;
    }

    public void setDefaultExchange(String defaultExchange) {
        this.defaultExchange = defaultExchange;
    }

    public String getDefaultRoutingKey() {
        return defaultRoutingKey;
    }

    public void setDefaultRoutingKey(String defaultRoutingKey) {
        this.defaultRoutingKey = defaultRoutingKey;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isTemplateMandatory() {
        return templateMandatory;
    }

    public void setTemplateMandatory(boolean templateMandatory) {
        this.templateMandatory = templateMandatory;
    }

    public MessageDeliveryMode getMessageDeliveryMode() {
        return messageDeliveryMode;
    }

    public void setMessageDeliveryMode(MessageDeliveryMode messageDeliveryMode) {
        this.messageDeliveryMode = messageDeliveryMode;
    }

    public boolean isChannelTransacted() {
        return channelTransacted;
    }

    public void setChannelTransacted(boolean channelTransacted) {
        this.channelTransacted = channelTransacted;
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

    public boolean isUseTemporaryReplyQueues() {
        return useTemporaryReplyQueues;
    }

    public void setUseTemporaryReplyQueues(boolean useTemporaryReplyQueues) {
        this.useTemporaryReplyQueues = useTemporaryReplyQueues;
    }

    public boolean isUseDirectReplyToContainer() {
        return useDirectReplyToContainer;
    }

    public void setUseDirectReplyToContainer(boolean useDirectReplyToContainer) {
        this.useDirectReplyToContainer = useDirectReplyToContainer;
    }

    public String getReplyAddress() {
        return replyAddress;
    }

    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }

    public String getCorrelationKey() {
        return correlationKey;
    }

    public void setCorrelationKey(String correlationKey) {
        this.correlationKey = correlationKey;
    }

    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public List<MessagePostProcessor> getBeforePublishPostProcessors() {
        return beforePublishPostProcessors;
    }

    public void setBeforePublishPostProcessors(List<MessagePostProcessor> beforePublishPostProcessors) {
        this.beforePublishPostProcessors = beforePublishPostProcessors;
    }

    public List<MessagePostProcessor> getAfterReceivePostProcessors() {
        return afterReceivePostProcessors;
    }

    public void setAfterReceivePostProcessors(List<MessagePostProcessor> afterReceivePostProcessors) {
        this.afterReceivePostProcessors = afterReceivePostProcessors;
    }

    public CachingConnectionFactory.ConfirmType getPublisherConfirmType() {
        return publisherConfirmType;
    }

    public void setPublisherConfirmType(CachingConnectionFactory.ConfirmType publisherConfirmType) {
        this.publisherConfirmType = publisherConfirmType;
    }

    public boolean isPublisherReturns() {
        return publisherReturns;
    }

    public void setPublisherReturns(boolean publisherReturns) {
        this.publisherReturns = publisherReturns;
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

    public RetryTemplate getRetryTemplate() {
        return retryTemplate;
    }

    public void setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    public RecoveryCallback<?> getRecoveryCallback() {
        return recoveryCallback;
    }

    public void setRecoveryCallback(RecoveryCallback<?> recoveryCallback) {
        this.recoveryCallback = recoveryCallback;
    }
}
