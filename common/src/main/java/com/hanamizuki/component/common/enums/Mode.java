package com.hanamizuki.component.common.enums;

public enum Mode {
    PLAINTEXT("text/plain"),

    //JSON_TEXT("text/x-json"),

    JSON("application/json"),

    //JAVA_JSON("application/x-java-serialized-object"),

    //XML("application/xml"),

    BYTES("application/octet-stream");

    final String contextType;

    public static Mode getMode(String contextType) {

        for (Mode value : Mode.values()) {
            if (value.name().equalsIgnoreCase(contextType)) {
                return value;
            }
        }
        return null;
    }

    Mode(String contextType) {
        this.contextType = contextType;
    }

    public String getContextType() {
        return contextType;
    }
}
