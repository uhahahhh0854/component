package com.raizumi.component.common.enums;

public enum Headers{
    AUTHORIZATION("Authorization"),

    COOKIE("Cookie"),

    CONTENT_TYPE("Content-Type"),

    USER_AGENT("User-Agent"),

    HOST("Host"),

    CACHE_CONTROL("Cache-Control"),

    EXPIRES("Expires"),

    ACCEPT("Accept"),

    ACCEPT_LANGUAGE("Accept-Language"),

    X_APP_KEY("X-App-Key"),

    X_SIGNATURE("X-Signature"),

    X_TIMESTAMP("X-Timestamp"),

    // Prevent replay attacks by verifying a unique, temporary nonce against a server-side cache.
    X_NONCE("X-Nonce"),

    X_VERSION("X-Version"),

    Y_PMATSEMIT("Y-Pmatsemit"),;

    final String name;

    Headers(String name){
        this.name = name;
    }
}