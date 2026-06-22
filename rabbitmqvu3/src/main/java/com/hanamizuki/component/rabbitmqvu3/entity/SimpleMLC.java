package com.hanamizuki.component.rabbitmqvu3.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BatchMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

/**
 * SimpleMessageListenerContainer 配置实体类
 * 继承 BaseMLC，保留 SimpleMLC 特有参数
 */

public class SimpleMLC extends BaseMLC {
    private final Logger log = LoggerFactory.getLogger(SimpleMLC.class);

    private int batchSize = 1;
    private boolean consumerBatchEnabled =  true;

    private int concurrentConsumers = 1;
    private int maxConcurrentConsumers = 1;
    private int declarationRetries = 3;
    private long failedDeclarationRetryInterval = 5000L;
    private ConditionalRejectingErrorHandler errorHandler;

    private boolean channelTransacted = false;
    private PlatformTransactionManager transactionManager;

    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConcurrentConsumers(concurrentConsumers);
        container.setMaxConcurrentConsumers(maxConcurrentConsumers);
        container.setDeclarationRetries(declarationRetries);
        container.setFailedDeclarationRetryInterval(failedDeclarationRetryInterval);
        if (errorHandler != null) {
            container.setErrorHandler(errorHandler);
        }else{
            container.setErrorHandler(new ConditionalRejectingErrorHandler());
        }

        // 设置 BaseMLC 公共字段
        container.setAutoStartup(isAutoStartup());
        container.setPhase(getPhase());
        container.setMissingQueuesFatal(isMissingQueuesFatal());
        container.setRecoveryInterval(getRecoveryInterval());
        container.setAcknowledgeMode(getAcknowledgeMode());
        container.setChannelTransacted(isChannelTransacted());

        if (isChannelTransacted()) {
            if (transactionManager != null) {
                container.setTransactionManager(transactionManager);
            }else{
                throw new IllegalStateException("Necessity transactionManager is null when channelTransacted is true");
            }
        }

        container.setPrefetchCount(getPrefetchCount());
        container.setBatchSize(getBatchSize());
        container.setConsumerBatchEnabled(isConsumerBatchEnabled());
        container.setExclusive(isExclusive());
        if (getQueueNames() == null && getQueues() == null) {
            throw new IllegalStateException("Queues or Queues are not set");
        }
        if (getQueueNames() != null) {
            container.setQueueNames(getQueueNames());
        }
        if (getQueues() != null) {
            container.setQueues(getQueues());
        }
        if (getMessageListener() != null) {
            if (!(isConsumerBatchEnabled() && (getMessageListener() instanceof BatchMessageListener || getMessageListener() instanceof ChannelAwareBatchMessageListener))) {
                throw new IllegalStateException("Consumer batch listener is required for SimpleMLC if batch enabled.");
            }
            container.setMessageListener(getMessageListener());
        }else {
            log.warn("Message listener is null, set in default");
            if (isConsumerBatchEnabled()){
                container.setMessageListener(new BatchMessageListener() {
                    @Override
                    public void onMessageBatch(List<Message> messages) {
                        System.out.println("onMessageBatch " + messages.size());
                    }
                });
            }else{
                container.setMessageListener(message -> log.warn("Received a message: {}", Arrays.toString(message.getBody())));
            }
        }

        if (getConsumerTagStrategy() != null) {
            container.setConsumerTagStrategy(getConsumerTagStrategy());
        }

        if (getTaskExecutor() != null) {
            container.setTaskExecutor(getTaskExecutor());
        }

        container.setDefaultRequeueRejected(isDefaultRequeueRejected());
        container.setIdleEventInterval(getIdleEventInterval());

        return container;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public boolean isConsumerBatchEnabled() {
        return consumerBatchEnabled;
    }

    public void setConsumerBatchEnabled(boolean consumerBatchEnabled) {
        this.consumerBatchEnabled = consumerBatchEnabled;
    }

    public int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(int concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public int getMaxConcurrentConsumers() {
        return maxConcurrentConsumers;
    }

    public void setMaxConcurrentConsumers(int maxConcurrentConsumers) {
        this.maxConcurrentConsumers = maxConcurrentConsumers;
    }

    public int getDeclarationRetries() {
        return declarationRetries;
    }

    public void setDeclarationRetries(int declarationRetries) {
        this.declarationRetries = declarationRetries;
    }

    public long getFailedDeclarationRetryInterval() {
        return failedDeclarationRetryInterval;
    }

    public void setFailedDeclarationRetryInterval(long failedDeclarationRetryInterval) {
        this.failedDeclarationRetryInterval = failedDeclarationRetryInterval;
    }

    public ConditionalRejectingErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ConditionalRejectingErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public boolean isChannelTransacted() {
        return channelTransacted;
    }

    public void setChannelTransacted(boolean channelTransacted) {
        this.channelTransacted = channelTransacted;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
