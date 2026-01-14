package com.raizumi.component.mqttvu3.pojo;

import com.raizumi.component.mqttvu3.exception.PahoException;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

public class ErrorMessage <T> implements Message<T>, Serializable {
    private static final long serialVersionUID = 4268801052358035099L;

    private final Exception exception;

    private final T payload;

    private final MessageHeaders headers;

    public ErrorMessage(Exception exception, T payload, MessageHeaders headers) {
        this.exception = exception;
        this.payload = payload;
        this.headers = headers;
    }

    @Override
    public T getPayload() {
        return this.payload;
    }

    @Override
    public MessageHeaders getHeaders() {
        return this.headers;
    }

    public static ErrorMessage<?> convert(Message<?> message, Exception exception) {
        return new ErrorMessage<>(exception ,message.getPayload(), message.getHeaders());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof ErrorMessage)) {
            return false;
        } else {
            ErrorMessage<?> otherMsg = (ErrorMessage<?>)other;
            return ObjectUtils.nullSafeEquals(this.payload, otherMsg.payload) && this.headers.equals(otherMsg.headers) && ObjectUtils.nullSafeEquals(this.exception, otherMsg.exception);
        }
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.payload) * 23 + this.headers.hashCode() + this.exception.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());

        sb.append(" [exception=");
        if (this.exception instanceof PahoException){
            PahoException pahoException = (PahoException) exception;
            sb.append(pahoException.getType().name()).append(" ").append(this.exception.getMessage()).append("]");
        }

        sb.append(" [payload=");
        if (this.payload instanceof byte[]) {
            sb.append("byte[").append(((byte[])((byte[])this.payload)).length).append(']');
        } else {
            sb.append(this.payload);
        }

        sb.append(", headers=").append(this.headers).append(']');
        return sb.toString();
    }

    public Exception getException() {
        return exception;
    }
}
