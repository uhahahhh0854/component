package com.hanamizuki.component.rabbitmqvu3;

import com.hanamizuki.component.rabbitmqvu3.annotation.ConditionalOnPrefix;
import com.hanamizuki.component.rabbitmqvu3.entity.Converter;
import com.hanamizuki.component.rabbitmqvu3.handler.Impl.DefaultConvertingProcessor;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@AutoConfigureBefore(RabbitAutoConfiguration.class)
@EnableConfigurationProperties(Converter.class)
@ConditionalOnPrefix(prefix = "spring.rabbitmq.converter")
public class BeforeRabbitAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public MessageConverter messageConverter(Converter converter) {
        converter.init();
        return new DefaultConvertingProcessor(converter);
    }

}
