package com.hanamizuki.component.rabbitmqvu3;


import com.hanamizuki.component.common.utils.ContextUtil;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RabbitmqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ContextUtil.class)
    public ContextUtil contextUtil(ObjectProvider<ApplicationContext> contextProvider) {
        return new ContextUtil(contextProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnSingleCandidate(ConnectionFactory.class)
    @ConditionalOnMissingBean(RabbitmqAdminister.class)
    public RabbitmqAdminister rabbitmqAdminister(ContextUtil contextUtil, ConnectionFactory connectionFactory) {
        return new RabbitmqAdminister(contextUtil, connectionFactory);
    }
}
