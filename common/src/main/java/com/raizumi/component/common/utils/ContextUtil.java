package com.raizumi.component.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Optional;

public class ContextUtil {

    private final Logger log = LoggerFactory.getLogger(ContextUtil.class);

    private final ApplicationContext applicationContext;

    public ContextUtil(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> boolean register(String beanName, Class<T> beanClass, T instance) {
        if (applicationContext.containsBean(beanName)) {
            log.warn("Bean [{}] already registered, skip operation.", beanName);
            return false;
        }
        try {
            if (applicationContext instanceof GenericApplicationContext) {
                GenericApplicationContext gac = (GenericApplicationContext) applicationContext;
                gac.registerBean(beanName, beanClass, () -> instance);
            } else {
                DefaultListableBeanFactory beanFactory =
                        (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                beanFactory.registerSingleton(beanName, instance);
            }
            log.info("Bean [{}] register successfully, type: [{}]", beanName, beanClass.getName());
            return true;
        } catch (Exception e) {
            log.error("Bean [{}] register fail: {}", beanName, e.getMessage(), e);
            return false;
        }
    }

    public boolean unregister(String beanName) {
        if (!applicationContext.containsBean(beanName)) {
            log.warn("Bean [{}] is not exist，remove skipping.", beanName);
            return false;
        }
        try {
            DefaultListableBeanFactory beanFactory =
                    (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            beanFactory.removeBeanDefinition(beanName);
            log.info("Bean [{}] remove successfully.", beanName);
            return true;
        } catch (Exception e) {
            log.error("Bean [{}] remove fail: {}", beanName, e.getMessage(), e);
            return false;
        }
    }

    public <T> Optional<T> getBean(String beanName, Class<T> clazz) {
        if (!applicationContext.containsBean(beanName)) {
            log.warn("Bean [{}] is not exist.", beanName);
            return Optional.empty();
        }
        return Optional.of(applicationContext.getBean(beanName, clazz));
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
