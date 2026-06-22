package com.hanamizuki.component.permission.auto;

import com.hanamizuki.component.common.auto.ComponentAutoConfiguration;
import com.hanamizuki.component.common.utils.ContextUtil;
import com.hanamizuki.component.permission.annotation.Permission;
import com.hanamizuki.component.permission.chain.ScrutinyDescriptor;
import com.hanamizuki.component.permission.handler.ScrutinyBus;
import com.hanamizuki.component.permission.entity.Security;
import com.hanamizuki.component.permission.PermissionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AutoConfiguration
@EnableConfigurationProperties(Security.class)
public class PermissionAutoConfiguration {


    private static final Logger log = LoggerFactory.getLogger(PermissionAutoConfiguration.class);

    @AutoConfigureAfter(ComponentAutoConfiguration.class)
    public static class ChainsAutoConfiguration {

        @Lazy
        @Resource
        private ContextUtil contextUtil;

        @Bean
        @ConditionalOnBean(PermissionEnabledMarker.class)
        public PermissionTemplate permissionProcessor(List<ScrutinyDescriptor> securities) {

            ScrutinyBus scrutinyBus = new ScrutinyBus(securities);

            boolean b = contextUtil.register("scrutinyBus", ScrutinyBus.class, scrutinyBus);

            if (b){
                log.error("Failed to register scrutiny bus.");
            }

            return new PermissionTemplate(scrutinyBus);
        }

        @Bean
        @ConditionalOnProperty(
                prefix = "utec.permission",
                name = {"module"}
        )
        public PermissionEnabledMarker  permissionEnabledMarker(Security security) {


            ApplicationContext applicationContext = contextUtil.getApplicationContext();

            Map<String, Object> mainBeans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);

            if (mainBeans.isEmpty()) {
                mainBeans = applicationContext.getBeansWithAnnotation(SpringBootConfiguration.class);
            }

            if (mainBeans.isEmpty()) {
                return null;
            }

            String basePackage = mainBeans.values().iterator().next().getClass().getPackage().getName();

            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(Permission.class));

            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);

            return !candidates.isEmpty() ? new PermissionEnabledMarker() : null;
        }

    }

    public static class PermissionEnabledMarker {

    }
}
