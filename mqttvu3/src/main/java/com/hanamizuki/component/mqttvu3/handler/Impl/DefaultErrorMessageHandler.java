package com.hanamizuki.component.mqttvu3.handler.Impl;

import com.hanamizuki.component.mqttvu3.exception.PahoException;
import com.hanamizuki.component.mqttvu3.handler.meta.AbstractErrorMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;


public class DefaultErrorMessageHandler extends AbstractErrorMessageHandler {
    private final Logger log = LoggerFactory.getLogger(DefaultErrorMessageHandler.class);

    @Override
    public void handleMessage(@NonNull Message<?> message) {
        try{
            if (!(message instanceof ErrorMessage)) {
                return;
            }
            ErrorMessage errorMessage = (ErrorMessage) message;

            if (!(errorMessage.getPayload() instanceof MessageTransformationException)) {
                return;
            }

            MessageTransformationException payload = (MessageTransformationException)errorMessage.getPayload();

            if (!(payload.getCause() instanceof PahoException)) {
                return;
            }

            PahoException cause = (PahoException) payload.getCause();

            switch (cause.getType()){
                case DE:
                    this.cryptErrorHandler(message);
                    break;
                case SE:
                    this.serializeErrorHandler(message);
                    break;
                case TRANSACTION:
                    this.transactionErrorHandler(message);
                    break;
            }
        }catch (Exception e){
            log.error("ErrorMessageHandler exception in ", e);
        }
    }

}
