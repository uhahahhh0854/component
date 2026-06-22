package com.hanamizuki.component.common.enums;

public enum HmacHeaders {
    X_APP_KEY("X-App-Key"),

    X_SIGNATURE("X-Signature"),

    X_TIMESTAMP("X-Timestamp"),

    // Prevent replay attacks by verifying a unique, temporary nonce against a server-side cache.
    X_NONCE("X-Nonce"),

    X_VERSION("X-Version");

    final String name;

    HmacHeaders(String name){
        this.name = name;
    }
}