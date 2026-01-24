package com.raizumi.component.mqttvu3;

import com.raizumi.component.mqttvu3.entity.Box;
import com.raizumi.component.common.utils.ContextUtil;

public class MqttAdminister {

    private final ContextUtil contextUtil;

    MqttAdminister(ContextUtil contextUtil){
        this.contextUtil = contextUtil;
    }

    public Mqttvu3Template register(Box box){
        return new Mqttvu3Template(box, contextUtil);
    }

}
