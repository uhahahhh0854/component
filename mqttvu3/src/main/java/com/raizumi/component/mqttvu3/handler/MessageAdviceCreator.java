package com.raizumi.component.mqttvu3.handler;

import com.raizumi.component.mqttvu3.entity.Vice;
import org.aopalliance.aop.Advice;
import org.springframework.messaging.MessageChannel;

public interface MessageAdviceCreator {

    Advice advice(Vice params);

    /*default Advice advice(Vice params){
        ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();

        if (params.getExpression() != null) {
            advice.setOnFailureExpressionString(params.getExpression());
        }
        advice.setTrapException(params.getIsTrap());
        return advice;
    }*/

    Advice advice(Vice viceParams, MessageChannel messageChannel);
    /*default Advice advice(Vice viceParams, MessageChannel messageChannel) {
        ExpressionEvaluatingRequestHandlerAdvice advice = (ExpressionEvaluatingRequestHandlerAdvice)advice(viceParams);
        advice.setFailureChannel(messageChannel);
        return advice;
    }*/

}
