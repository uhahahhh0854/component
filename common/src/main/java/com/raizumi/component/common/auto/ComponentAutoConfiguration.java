package com.raizumi.component.common.auto;

import com.raizumi.component.common.utils.ContextUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ComponentAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ContextUtil.class)
    public ContextUtil contextUtil(ObjectProvider<ApplicationContext> contextProvider) {
        return new ContextUtil(contextProvider.getIfAvailable());
    }

}
