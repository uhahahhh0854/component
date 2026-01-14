package com.raizumi.component.common.tool;

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
            log.warn("Bean [{}] 已存在，注册跳过。", beanName);
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
            log.info("Bean [{}] 注册成功，类型 [{}]", beanName, beanClass.getName());
            return true;
        } catch (Exception e) {
            log.error("Bean [{}] 注册失败: {}", beanName, e.getMessage(), e);
            return false;
        }
    }

    public boolean unregister(String beanName) {
        if (!applicationContext.containsBean(beanName)) {
            log.warn("Bean [{}] 不存在，注销跳过。", beanName);
            return false;
        }
        try {
            DefaultListableBeanFactory beanFactory =
                    (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            beanFactory.removeBeanDefinition(beanName);
            log.info("Bean [{}] 注销成功", beanName);
            return true;
        } catch (Exception e) {
            log.error("Bean [{}] 注销失败: {}", beanName, e.getMessage(), e);
            return false;
        }
    }

    public <T> Optional<T> getBean(String beanName, Class<T> clazz) {
        if (!applicationContext.containsBean(beanName)) {
            log.warn("Bean [{}] 不存在", beanName);
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
