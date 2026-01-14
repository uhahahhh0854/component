package com.raizumi.component.mqttvu3.handler.meta;

import com.raizumi.component.mqttvu3.handler.ErrorMessageHandler;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;

public abstract class AbstractErrorMessageHandler implements ErrorMessageHandler {

    public abstract void handleMessage(@NonNull Message<?> message);
}
