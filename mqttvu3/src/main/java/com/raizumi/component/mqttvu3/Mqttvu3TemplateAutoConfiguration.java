package com.raizumi.component.mqttvu3;

import com.raizumi.component.mqttvu3.entity.Box;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DefaultHeaderChannelRegistry;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.channel.HeaderChannelRegistry;
import org.springframework.messaging.MessageChannel;

@AutoConfiguration(after = Mqttvu3AdministerAutoConfiguration.class)
@EnableConfigurationProperties(Box.class)
@ConditionalOnBean(Mqttvu3Administer.class)
public class Mqttvu3TemplateAutoConfiguration {

    @Bean
    @ConditionalOnProperty(
            prefix = "utec.mqtt",
            name = {"host", "username", "password", "auto-create"}
    )
    public Mqttvu3Template mqttvu3Template(Box box, Mqttvu3Administer administer) {
        System.out.println(box.toString());
        return administer.register(box);
    }

    @Bean
    @ConditionalOnMissingBean(name = "integrationHeaderChannelRegistry")
    public HeaderChannelRegistry integrationHeaderChannelRegistry() {
        return new DefaultHeaderChannelRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(name = "errorChannel")
    public MessageChannel errorChannel() {
        return new PublishSubscribeChannel();
    }
}