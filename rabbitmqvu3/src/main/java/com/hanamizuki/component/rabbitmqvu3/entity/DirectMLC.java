package com.hanamizuki.component.rabbitmqvu3.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;

import java.util.Arrays;

/**
 * DirectMessageListenerContainer 配置实体类
 * 继承 BaseMLC，保留 DirectMLC 特有参数
 */
public class DirectMLC extends BaseMLC {
    private final Logger log = LoggerFactory.getLogger(DirectMLC.class);

    private int consumersPerQueue = 1;  // 每个队列的底层消费者数

    public DirectMessageListenerContainer directMessageListenerContainer() {

        DirectMessageListenerContainer container = new DirectMessageListenerContainer();

        // DirectMLC 特有参数
        container.setConsumersPerQueue(consumersPerQueue);

        // 设置 BaseMLC 公共字段
        container.setAutoStartup(isAutoStartup());
        container.setPhase(getPhase());
        container.setMissingQueuesFatal(isMissingQueuesFatal());
        container.setRecoveryInterval(getRecoveryInterval());
        container.setAcknowledgeMode(getAcknowledgeMode());
        container.setPrefetchCount(getPrefetchCount());
        container.setExclusive(isExclusive());

        if (getQueueNames() == null && getQueues() == null) {
            throw new IllegalStateException("Queues or Queues are not set");
        }
        if (getQueues() != null) {
            container.setQueues(getQueues());
        }
        if (getQueueNames() != null) {
            container.setQueueNames(getQueueNames());
        }

        if (getMessageListener() != null) {
            container.setMessageListener(getMessageListener());
        }else {
            log.warn("Message listener is null, set in default");
            container.setMessageListener(message -> log.warn("Received a message: {}", Arrays.toString(message.getBody())));
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

    public int getConsumersPerQueue() {
        return consumersPerQueue;
    }

    public void setConsumersPerQueue(int consumersPerQueue) {
        this.consumersPerQueue = consumersPerQueue;
    }
}
