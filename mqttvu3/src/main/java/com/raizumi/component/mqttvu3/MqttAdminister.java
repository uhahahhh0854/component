package com.raizumi.component.mqttvu3;

import com.raizumi.component.mqttvu3.entity.Box;
import com.raizumi.component.common.tool.ContextUtil;

public class MqttAdminister {

    private final ContextUtil contextUtil;

    MqttAdminister(ContextUtil contextUtil){
        this.contextUtil = contextUtil;
    }

    public JosteinMqttTemplate register(Box box){
        return new JosteinMqttTemplate(box, contextUtil);
    }

}
