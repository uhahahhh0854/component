package com.hanamizuki.component.permission.annotation;

import java.lang.annotation.*;

/**
 * Automatically derive permission identifiers from RequestMapping URLS, explicit identifiers will be overridden.
 * Explicit identifiers have higher priority when defined at the method.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String value() default "";
}

