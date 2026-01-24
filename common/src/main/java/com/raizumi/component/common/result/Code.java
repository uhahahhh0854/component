package com.raizumi.component.common.result;

public enum Code {
    SUCCESS(200, "OK"),
    CREATED(201, "Created"),

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    CONFLICT(409, "Conflict"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    INTERNAL_SERVER_ERROR(500, "Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    BUSINESS_ERROR(1000, "Business Logic Error"),
    DATA_INTEGRITY_VIOLATION(1001, "Data Violation"),
    FILE_UPLOAD_ERROR(1002, "Upload Failed"),
    USER_LOCKED(1003, "Account Locked");

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
