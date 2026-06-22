package com.hanamizuki.component.rabbitmqvu3.annotation.Impl;

import com.hanamizuki.component.rabbitmqvu3.annotation.ConditionalOnPrefix;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class PrefixCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        Map<String, Object> attrs = metadata.getAnnotationAttributes(
                ConditionalOnPrefix.class.getName());

        assert attrs != null;
        String prefix = (String) attrs.get("prefix");

        // 尝试直接查找精确前缀（比如 spring.rabbitmq.converter）
        boolean found = env.getProperty(prefix) != null;

        // 遍历所有 PropertySource，看是否存在以 prefix. 开头的属性
        if (!found && env instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment configurableEnv = (ConfigurableEnvironment) env;
            for (PropertySource<?> source : configurableEnv.getPropertySources()) {
                Object src = source.getSource();
                if (src instanceof Map) {
                    for (Object keyObj : ((Map<?, ?>) src).keySet()) {
                        String key = keyObj.toString();
                        if (key.startsWith(prefix + ".")) {
                            found = true;
                            break;
                        }
                    }
                }
                if (found) break;
            }
        }

        if (found) {
            return ConditionOutcome.match("Found property prefix: " + prefix);
        } else {
            return ConditionOutcome.noMatch("No property starts with prefix: " + prefix);
        }
    }
}


