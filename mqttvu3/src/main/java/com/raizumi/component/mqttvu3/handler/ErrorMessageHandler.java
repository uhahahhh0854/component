package com.raizumi.component.mqttvu3.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

public interface ErrorMessageHandler extends MessageHandler {

    default void defaultErrorHandler(Message<?> message) {
        System.out.println(message.toString());
    }

    default void cryptErrorHandler(Message<?> message) {
        System.out.println(message.toString());
    }

    default void serializeErrorHandler(Message<?> message){
        System.out.println(message.toString());
    }

    default void transactionErrorHandler(Message<?> message){
        System.out.println(message.toString());
    }

}
