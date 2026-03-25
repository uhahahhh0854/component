package com.raizumi.component.mqttvu3;

import com.raizumi.component.mqttvu3.entity.Box;
import com.raizumi.component.common.utils.ContextUtil;

public class Mqttvu3Administer {

    private final ContextUtil contextUtil;

    Mqttvu3Administer(ContextUtil contextUtil){
        this.contextUtil = contextUtil;
    }

    public Mqttvu3Template register(Box box){
        return new Mqttvu3Template(box, contextUtil);
    }

}
