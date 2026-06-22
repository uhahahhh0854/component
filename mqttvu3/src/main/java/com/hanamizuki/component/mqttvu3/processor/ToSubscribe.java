package com.hanamizuki.component.mqttvu3.processor;

import com.hanamizuki.component.mqttvu3.entity.Disposer;
import com.hanamizuki.component.mqttvu3.entity.Sub;
import com.hanamizuki.component.common.handler.DEHandleDescriptor;
import com.hanamizuki.component.common.handler.SEHandleDescriptor;
import com.hanamizuki.component.mqttvu3.handler.TransformingDescriptor;
import org.springframework.messaging.MessageHandler;

import java.util.List;

public interface ToSubscribe {

    List<String> subscribe(Sub sub);

    List<String> subscribe(MessageHandler messageHandler, String ... topics);

    List<String> subscribe(MessageHandler messageHandler,
                     MessageHandler errorMessageHandler,
                     String ... topics);

    List<String> subscribe(MessageHandler messageHandler,
                     Disposer disposer,
                     String ... topics);

    List<String> subscribe(MessageHandler messageHandler,
                     MessageHandler errorMessageHandler,
                     Disposer disposer,
                     String ... topics);

    List<String> subscribe(MessageHandler messageHandler,
                     TransformingDescriptor transformingDescriptor,
                     String ... topics);

    List<String> subscribe(MessageHandler messageHandler,
                     DEHandleDescriptor deHandleDescriptor,
                     SEHandleDescriptor seHandleDescriptor,
                     String ... topics);
}
