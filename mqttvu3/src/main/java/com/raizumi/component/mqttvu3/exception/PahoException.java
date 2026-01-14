package com.raizumi.component.mqttvu3.exception;

public class PahoException extends RuntimeException {

    public enum Type{
        ELSE,
        INIT,
        DE,
        SE,
        TRANSACTION
    }

    private final Type type;

    public PahoException(){
        super();
        this.type = Type.ELSE;
    }

    public PahoException(Type type){
        super();
        this.type = type;
    }

    public PahoException(String message, Type type) {
        super(message);
        this.type = type;
    }

    public PahoException(String message, Throwable cause, Type type) {
        super(message, cause);
        this.type = type;
    }

    public PahoException(Throwable cause, Type type) {
        super(cause);
        this.type = type;
    }

    public PahoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Type type) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.type = type;
    }


    public PahoException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
