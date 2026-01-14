package com.raizumi.component.common.tool;


import java.util.UUID;

public class UuidUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static <T> Integer hash(T t) {
        int hashCode = (UUID.randomUUID().toString().replace("-", "") + t.toString()).hashCode();
        return hashCode < 0 ? -hashCode : hashCode;
    }

    public static String uuid(char prefix){
        long millis = System.currentTimeMillis();
        String timestamp = String.valueOf(millis);

        return prefix + new StringBuilder(timestamp).reverse().toString();
    }
}
