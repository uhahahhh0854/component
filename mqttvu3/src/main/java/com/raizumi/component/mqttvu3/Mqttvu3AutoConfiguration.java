package com.raizumi.component.mqttvu3;

import com.raizumi.component.mqttvu3.entity.Box;
import com.raizumi.component.common.utils.ContextUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.channel.DefaultHeaderChannelRegistry;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.channel.HeaderChannelRegistry;
import org.springframework.messaging.MessageChannel;

@AutoConfiguration
@Import({
        Mqttvu3AutoConfiguration.DefaultAutoConfiguration.class,
        Mqttvu3AutoConfiguration.BoxAutoConfiguration.class
})
public class Mqttvu3AutoConfiguration {

    private static Mqttvu3Administer mqttvu3Administer;

    @AutoConfiguration
    public static class DefaultAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(ContextUtil.class)
        public ContextUtil contextUtil(ObjectProvider<ApplicationContext> contextProvider) {
            return new ContextUtil(contextProvider.getIfAvailable());
        }

        @Bean
        @ConditionalOnMissingBean(Mqttvu3Administer.class) // 确保唯一
        public Mqttvu3Administer mqttAdminister(ContextUtil contextUtil) {
            mqttvu3Administer = new Mqttvu3Administer(contextUtil);
            return mqttvu3Administer;
        }
    }

    @AutoConfiguration(after = DefaultAutoConfiguration.class)
    @EnableConfigurationProperties(Box.class)
    public static class BoxAutoConfiguration {

        @Bean
        @ConditionalOnBean(Mqttvu3Administer.class)
        @ConditionalOnProperty(
                prefix = "utec.mqtt",
                name = {"host", "username", "password"}
        )
        public Mqttvu3Template mqttTemplate(Box box) {
            return mqttvu3Administer.register(box);
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
}
