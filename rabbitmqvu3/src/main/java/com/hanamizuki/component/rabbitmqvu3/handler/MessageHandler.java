package com.hanamizuki.component.rabbitmqvu3.handler;

import java.util.List;

public interface MessageHandler <T> {
    void handleMessage(T message);

    void handleMessages(List<T> messages);
}
