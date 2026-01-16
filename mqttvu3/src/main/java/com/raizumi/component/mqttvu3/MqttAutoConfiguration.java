package com.raizumi.component.mqttvu3;

import com.raizumi.component.mqttvu3.entity.Box;
import com.raizumi.component.common.tool.ContextUtil;
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
        MqttAutoConfiguration.DefaultAutoConfiguration.class,
        MqttAutoConfiguration.BoxAutoConfiguration.class
})
public class MqttAutoConfiguration {

    private static MqttAdminister mqttAdminister;

    @AutoConfiguration
    public static class DefaultAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(ContextUtil.class)
        public ContextUtil contextUtil(ObjectProvider<ApplicationContext> contextProvider) {
            return new ContextUtil(contextProvider.getIfAvailable());
        }

        @Bean
        @ConditionalOnMissingBean(MqttAdminister.class) // 确保唯一
        public MqttAdminister mqttAdminister(ContextUtil contextUtil) {
            mqttAdminister = new MqttAdminister(contextUtil);
            return mqttAdminister;
        }
    }

    @AutoConfiguration(after = DefaultAutoConfiguration.class)
    @EnableConfigurationProperties(Box.class)
    public static class BoxAutoConfiguration {

        @Bean
        @ConditionalOnBean(MqttAdminister.class)
        @ConditionalOnProperty(
                prefix = "joinstec.mqtt",
                name = {"host", "username", "password"}
        )
        public Mqttvu3Template mqttTemplate(Box box) {
            return mqttAdminister.register(box);
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
