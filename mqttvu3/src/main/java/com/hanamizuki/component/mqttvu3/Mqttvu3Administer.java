package com.hanamizuki.component.mqttvu3;

import com.hanamizuki.component.mqttvu3.entity.Box;
import com.hanamizuki.component.common.utils.ContextUtil;


public class Mqttvu3Administer {

    private final ContextUtil contextUtil;

    Mqttvu3Administer(ContextUtil contextUtil){
        this.contextUtil = contextUtil;
    }

    public Mqttvu3Template register(String beanName,  Box box){
        Mqttvu3Template mqttvu3Template = new Mqttvu3Template(box, contextUtil);

        contextUtil.register(beanName, Mqttvu3Template.class, mqttvu3Template);

        return mqttvu3Template;
    }

    public Mqttvu3Template register(Box box){
        return new Mqttvu3Template(box, contextUtil);
    }

}
