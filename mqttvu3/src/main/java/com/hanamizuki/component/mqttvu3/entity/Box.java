package com.hanamizuki.component.mqttvu3.entity;

import com.hanamizuki.component.mqttvu3.handler.Impl.DefaultErrorMessageHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.messaging.MessageHandler;

import java.io.Serializable;
import java.util.UUID;

@ConfigurationProperties(
        prefix = "utec.mqtt"
)
public class Box  extends Disposer implements Serializable, Connection {

    public Box(){
    }

    public void process() {
        super.process();
    }

    /*  连接参数*/
    private String host = "tcp://localhost:1883";

    private String clientId = UUID.randomUUID().toString();

    private String username = "guess";

    private String password = "guess";

    private String defaultPublishTopic;

    private Integer qos = 2;

    private Integer connectionTimeOut = 30;

    private Integer keepAlive = 60;

    private Boolean cleanSession = true;

    private Boolean automaticReconnect = true;

    /*  错误处理器，推荐使用封装的ErrorMessageHandler*/
    private MessageHandler errorMessageHandler = new DefaultErrorMessageHandler();

    private Ssl ssl;

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

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
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

    public MessageHandler getErrorMessageHandler() {
        return errorMessageHandler;
    }

    public void setErrorMessageHandler(MessageHandler errorMessageHandler) {
        this.errorMessageHandler = errorMessageHandler;
    }

    public Ssl getSsl() {
        return ssl;
    }

    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    public void setDisposer(Disposer disposer) {
        this.setVice(disposer.getVice());
        this.setClazz(disposer.getClazz());
        this.setSecretKey(disposer.getSecretKey());
        this.setAlgorithm(disposer.getAlgorithm());
        this.setMode(disposer.getMode());
        this.setEncoding(disposer.getEncoding());
    }

    @Override
    public String toString() {
        return "Box{" +
                "host='" + host + '\'' + ", " + "\n" +
                "clientId='" + clientId + '\'' + ", " + "\n" +
                "username='" + username + '\'' + ", " + "\n" +
                "password='" + password + '\'' + ", " + "\n" +
                "defaultTopic='" + defaultPublishTopic + '\'' + ", " + "\n" +
                "qos=" + qos + ", " + "\n" +
                "connectionTimeOut=" + connectionTimeOut + ", " + "\n" +
                "keepAlive=" + keepAlive + ", " + "\n" +
                "cleanSession=" + cleanSession + ", " + "\n" +
                "automaticReconnect=" + automaticReconnect + ", " + "\n" +
                "errorMessageHandler=" + errorMessageHandler + ", " + "\n" +
                "ssl=" + ssl +
                '}';
    }

    public String getDefaultPublishTopic() {
        return defaultPublishTopic;
    }

    public void setDefaultPublishTopic(String defaultPublishTopic) {
        this.defaultPublishTopic = defaultPublishTopic;
    }

    public Sub tosub() {
        Sub sub = new Sub();
        sub.setHost(host);

        sub.setUsername(username);
        sub.setPassword(password);
        sub.setSsl(ssl);

        return sub;
    }
}
