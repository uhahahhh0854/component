package com.raizumi.component.rabbitmqvu3;


import com.raizumi.component.common.tool.ContextUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @ConditionalOnMissingBean(RabbitmqAdminister.class)
    public RabbitmqAdminister rabbitmqAdminister(ContextUtil contextUtil) {
        return new RabbitmqAdminister(contextUtil);
    }
}
