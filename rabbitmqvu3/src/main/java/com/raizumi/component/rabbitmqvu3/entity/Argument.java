package com.raizumi.component.rabbitmqvu3.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * Spring AMQP 默认使用的消息 headers，包括：
 * 1. Jackson 多态反序列化字段：typeId, contentTypeId, keyTypeId, valueTypeId
 * 2. 发布失败回调字段：returnedMessageCorrelation
 * 3. RabbitMQ 特殊功能字段：xDelay（延迟队列）, xPriority（优先级队列）
 * 4. RabbitMQ 自动生成字段：xDeath（死信队列记录）
 */

public class Argument implements Serializable {
    private String typeId;
    private String contentTypeId;
    private String keyTypeId;
    private String valueTypeId;

    private String returnedMessageCorrelation;

    private Integer xDelay;
    private Integer xPriority;

    private List<Map<String, Object>> xDeath;

    public Argument(String typeId, String contentTypeId, String keyTypeId, String valueTypeId, String returnedMessageCorrelation, Integer xDelay, Integer xPriority, List<Map<String, Object>> xDeath) {
        this.typeId = typeId;
        this.contentTypeId = contentTypeId;
        this.keyTypeId = keyTypeId;
        this.valueTypeId = valueTypeId;
        this.returnedMessageCorrelation = returnedMessageCorrelation;
        this.xDelay = xDelay;
        this.xPriority = xPriority;
        this.xDeath = xDeath;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public String getKeyTypeId() {
        return keyTypeId;
    }

    public void setKeyTypeId(String keyTypeId) {
        this.keyTypeId = keyTypeId;
    }

    public String getValueTypeId() {
        return valueTypeId;
    }

    public void setValueTypeId(String valueTypeId) {
        this.valueTypeId = valueTypeId;
    }

    public String getReturnedMessageCorrelation() {
        return returnedMessageCorrelation;
    }

    public void setReturnedMessageCorrelation(String returnedMessageCorrelation) {
        this.returnedMessageCorrelation = returnedMessageCorrelation;
    }

    public Integer getxDelay() {
        return xDelay;
    }

    public void setxDelay(Integer xDelay) {
        this.xDelay = xDelay;
    }

    public Integer getxPriority() {
        return xPriority;
    }

    public void setxPriority(Integer xPriority) {
        this.xPriority = xPriority;
    }

    public List<Map<String, Object>> getxDeath() {
        return xDeath;
    }

    public void setxDeath(List<Map<String, Object>> xDeath) {
        this.xDeath = xDeath;
    }
}