package com.hanamizuki.component.rabbitmqvu3.annotation;


import com.hanamizuki.component.rabbitmqvu3.annotation.Impl.PrefixCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(PrefixCondition.class)
public @interface ConditionalOnPrefix {
    String prefix();
}
