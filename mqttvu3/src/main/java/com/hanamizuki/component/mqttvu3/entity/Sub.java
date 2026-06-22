package com.hanamizuki.component.mqttvu3.entity;

import com.hanamizuki.component.mqttvu3.handler.Impl.DefaultErrorMessageHandler;
import org.springframework.messaging.MessageHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class Sub extends Disposer implements Serializable, Connection {

    public Sub(){
    }

    public Sub(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private String host = "tcp://localhost:1883";

    private String clientId = UUID.randomUUID().toString();

    private String username = "guess";

    private String password = "guess";

    private Integer connectionTimeOut = 30;

    private Integer keepAlive = 60;

    private Boolean cleanSession = true;

    private Boolean automaticReconnect = true;

    private String[] topics;

    private int[] qoses;

    private Boolean isBranch = false;

    private Integer batchSize = 10;

    /*  业务处理器*/
    private MessageHandler messageHandler = message -> {};

    /*  错误处理器，推荐使用封装的ErrorMessageHandler*/
    private MessageHandler errorMessageHandler = new DefaultErrorMessageHandler();

    /*  是否立即启动*/
    private Boolean activate = true;

    private Ssl ssl;

    public void setDisposer(Disposer disposer) {
        this.setVice(disposer.getVice());
        this.setClazz(disposer.getClazz());
        this.setSecretKey(disposer.getSecretKey());
        this.setAlgorithm(disposer.getAlgorithm());
        this.setMode(disposer.getMode());
        this.setEncoding(disposer.getEncoding());
    }


    /**
     * 只在 broker支持的情况下使用
     * */

    private int concurrency = 1;

    private String groupId = "default";

    private boolean shared = false;


    public String valid(){
        StringBuilder sb = new StringBuilder();

        return  sb.length() == 0? null : sb.toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(Integer connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public Integer getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Integer keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(Boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public Boolean getAutomaticReconnect() {
        return automaticReconnect;
    }

    public void setAutomaticReconnect(Boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;

        int[] qoses = new int[topics.length];
        for (int i = 0; i < topics.length; i++) {
            qoses[i] = 2;
        }
        this.qoses = qoses;
    }

    public void setTopics(String[] topics, int[] qoses) {
        this.topics = topics;
        this.qoses = qoses;
    }

    public int[] getQoses() {
        return qoses;
    }

    public void setQoses(int[] qoses) {
        this.qoses = qoses;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public MessageHandler getErrorMessageHandler() {
        return errorMessageHandler;
    }

    public void setErrorMessageHandler(MessageHandler errorMessageHandler) {
        this.errorMessageHandler = errorMessageHandler;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Ssl getSsl() {
        return ssl;
    }

    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Boolean getBranch() {
        return isBranch;
    }

    public void setBranch(Boolean branch) {
        isBranch = branch;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public String toString() {
        return "Sub{" + "\n" +
                "host='" + host + '\'' + ", " + "\n" +
                "clientId='" + clientId + '\'' + ", " + "\n" +
                "username='" + username + '\'' + ", " + "\n" +
                "password='" + password + '\'' + ", " + "\n" +
                "connectionTimeOut=" + connectionTimeOut + ", " + "\n" +
                "keepAlive=" + keepAlive + ", " + "\n" +
                "cleanSession=" + cleanSession + ", " + "\n" +
                "automaticReconnect=" + automaticReconnect + ", " + "\n" +
                "topics=" + Arrays.toString(topics) + ", " + "\n" +
                "qos=" + Arrays.toString(qoses) + ", " + "\n" +
                "isBranch=" + isBranch + ", " + "\n" +
                "batchSize=" + batchSize + ", " + "\n" +
                "messageHandler=" + messageHandler + ", " + "\n" +
                "errorMessageHandler=" + errorMessageHandler + ", " + "\n" +
                "activate=" + activate + ", " + "\n" +
                "ssl=" + ssl + ", " + "\n" +
                "concurrency=" + concurrency + ", " + "\n" +
                "groupId='" + groupId + '\'' + ", " + "\n" +
                "shared=" + shared + "\n" +
                '}';
    }
}