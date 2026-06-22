package com.hanamizuki.component.mqttvu3.handler.Impl;

import com.hanamizuki.component.mqttvu3.entity.Vice;
import com.hanamizuki.component.mqttvu3.handler.MessageAdviceCreator;
import org.aopalliance.aop.Advice;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice;
import org.springframework.messaging.MessageChannel;

public class DefaultMessageAdviceCreator implements MessageAdviceCreator {

    private ExpressionEvaluatingRequestHandlerAdvice advice;

    public DefaultMessageAdviceCreator(Vice vice) {
        advice = (ExpressionEvaluatingRequestHandlerAdvice)this.advice(vice);
    }

    public void setErrorChannel(MessageChannel messageChannel) {
        advice.setFailureChannel(messageChannel);
    }

    public void setErrorChannel(String channelName) {
        advice.setFailureChannelName(channelName);
    }

    @Override
    public Advice advice(Vice params) {
        ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();

        if (params.getExpression() != null) {
            advice.setOnFailureExpressionString(params.getExpression());
        }
        advice.setTrapException(params.getTrap());
        return advice;
    }

    @Override
    public Advice advice(Vice viceParams, MessageChannel messageChannel) {
        advice.setFailureChannel(messageChannel);
        return advice;
    }

    public ExpressionEvaluatingRequestHandlerAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(ExpressionEvaluatingRequestHandlerAdvice advice) {
        this.advice = advice;
    }
}
