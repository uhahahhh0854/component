package com.raizumi.component.mqttvu3.entity;

import org.springframework.integration.endpoint.MessageProducerSupport;

import java.util.List;

public class SubInfo {
    private String flowId;

    private String clientId;

    private MessageProducerSupport support;

    private List<String> topics;

    private boolean active;

    // 私有构造函数（外部不能直接 new）
    private SubInfo(Builder builder) {
        this.flowId = builder.flowId;
        this.clientId = builder.clientId;
        this.support = builder.support;
        this.topics = builder.topics;
        this.active = builder.active;
    }

    // 静态方法：创建 Builder 实例
    public static Builder builder() {
        return new Builder();
    }

    // 静态内部类：Builder
    public static class Builder {
        private String flowId;
        private String clientId;
        private MessageProducerSupport support;
        private List<String> topics;
        private boolean active;

        public Builder flowId(String flowId) {
            this.flowId = flowId;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder support(MessageProducerSupport support) {
            this.support = support;
            return this;
        }

        public Builder topics(List<String> topics) {
            this.topics = topics;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public SubInfo build() {
            return new SubInfo(this);
        }
    }


    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MessageProducerSupport getSupport() {
        return support;
    }

    public void setSupport(MessageProducerSupport support) {
        this.support = support;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
