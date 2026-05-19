package com.raizumi.component.mqttvu3.processor;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public interface Sender {

    void send(@Header(MqttHeaders.TOPIC) String topic,
              @Payload Object payload);

    void send(@Header(MqttHeaders.TOPIC) String topic,
              @Header(MqttHeaders.QOS) int qos,
              @Payload Object payload);

}
