package com.raizumi.component.common.result;

public enum Code {
    SUCCESS(200, "Success"),

    BAD_REQUEST(400, "Bad request"),

    UNAUTHORIZED(401,"Unauthorized"),

    INTERNAL_ERROR(500, "Internal error"),;

    final int status;

    final String msg;

    Code(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
