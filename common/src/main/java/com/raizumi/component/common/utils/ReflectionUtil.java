package com.raizumi.component.common.utils;

import java.lang.reflect.Method;

public class ReflectionUtil {
    public static boolean isOverride(Class<?> clazz, String ... methodNames) {

        for (String name : methodNames) {
            try{
                Method method = clazz.getMethod(name);
                if (method.getDeclaringClass() == clazz) {
                    return true;
                }

            }catch (NoSuchMethodException e){
                return false;
            }
        }
        return false;
    }

    public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            clazz.getMethod(methodName, paramTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}
