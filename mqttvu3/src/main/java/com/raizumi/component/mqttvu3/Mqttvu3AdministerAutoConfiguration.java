package com.raizumi.component.mqttvu3;

import com.raizumi.component.common.utils.ContextUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class Mqttvu3AdministerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ContextUtil.class)
    public ContextUtil contextUtil(ObjectProvider<ApplicationContext> contextProvider) {
        return new ContextUtil(contextProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(Mqttvu3Administer.class)
    public Mqttvu3Administer mqttAdminister(ContextUtil contextUtil) {
        return new Mqttvu3Administer(contextUtil);
    }
}
