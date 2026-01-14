package com.raizumi.component.rabbitmqvu3.entity;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.core.task.TaskExecutor;

/**
 * RabbitMQ 消费者公共配置
 * 适用于 SimpleMessageListenerContainer 和 DirectMessageListenerContainer
 * 包含连接、队列、消息确认、事务、监听器、异常处理、重试等公共参数
 */
public class BaseMLC {

    private boolean autoStartup = true;
    private int phase = 0;
    private boolean missingQueuesFatal = true;
    private long recoveryInterval = 5000L;

    private String[] queueNames;
    private Queue[] queues;
    private boolean exclusive = false;

    private AcknowledgeMode acknowledgeMode = AcknowledgeMode.AUTO;

    private int prefetchCount = 1;

    private Converter converter;

    private MessageListener messageListener;

    private ConsumerTagStrategy consumerTagStrategy;

    private TaskExecutor taskExecutor;
    private Advice[] adviceChain;
    private boolean defaultRequeueRejected = true;
    private long idleEventInterval = 0L;

    public boolean isAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public boolean isMissingQueuesFatal() {
        return missingQueuesFatal;
    }

    public void setMissingQueuesFatal(boolean missingQueuesFatal) {
        this.missingQueuesFatal = missingQueuesFatal;
    }

    public long getRecoveryInterval() {
        return recoveryInterval;
    }

    public void setRecoveryInterval(long recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    public String[] getQueueNames() {
        return queueNames;
    }

    public void setQueueNames(String[] queueNames) {
        this.queueNames = queueNames;
    }

    public Queue[] getQueues() {
        return queues;
    }

    public void setQueues(Queue[] queues) {
        this.queues = queues;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public AcknowledgeMode getAcknowledgeMode() {
        return acknowledgeMode;
    }

    public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    public int getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public ConsumerTagStrategy getConsumerTagStrategy() {
        return consumerTagStrategy;
    }

    public void setConsumerTagStrategy(ConsumerTagStrategy consumerTagStrategy) {
        this.consumerTagStrategy = consumerTagStrategy;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public Advice[] getAdviceChain() {
        return adviceChain;
    }

    public void setAdviceChain(Advice[] adviceChain) {
        this.adviceChain = adviceChain;
    }

    public boolean isDefaultRequeueRejected() {
        return defaultRequeueRejected;
    }

    public void setDefaultRequeueRejected(boolean defaultRequeueRejected) {
        this.defaultRequeueRejected = defaultRequeueRejected;
    }

    public long getIdleEventInterval() {
        return idleEventInterval;
    }

    public void setIdleEventInterval(long idleEventInterval) {
        this.idleEventInterval = idleEventInterval;
    }
}
