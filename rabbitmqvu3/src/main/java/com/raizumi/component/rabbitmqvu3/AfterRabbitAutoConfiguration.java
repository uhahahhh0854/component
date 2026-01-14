package com.raizumi.component.rabbitmqvu3;


import com.raizumi.component.common.tool.ContextUtil;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class AfterRabbitAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    protected static class PrefixCreator {
        @Bean
        @ConditionalOnMissingBean(ContextUtil.class)
        public ContextUtil contextUtil(ObjectProvider<ApplicationContext> contextProvider) {
            return new ContextUtil(contextProvider.getIfAvailable());
        }
    }

    @Configuration(proxyBeanMethods = false)
    @Import(PrefixCreator.class)
    public static class AfterCreatorConfiguration {
        @Bean
        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        public JosteinRabbitmqTemplate josteinRabbitmqTemplate(RabbitTemplateConfigurer configurer,
                                                              ConnectionFactory connectionFactory,
                                                              ContextUtil contextUtil) {

            if (configurer == null) {
                throw new IllegalArgumentException("contextUtil is null");
            }

            JosteinRabbitmqTemplate template = new JosteinRabbitmqTemplate(contextUtil,connectionFactory);
            configurer.configure(template, connectionFactory);
            return template;
        }
    }
}
