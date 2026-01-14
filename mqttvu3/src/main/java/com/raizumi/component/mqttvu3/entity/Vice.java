package com.raizumi.component.mqttvu3.entity;

public class Vice {
    private String expression;

    private Boolean isTrap = false;

    public Boolean getTrap() {
        return isTrap;
    }

    public void setTrap(Boolean trap) {
        isTrap = trap;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}