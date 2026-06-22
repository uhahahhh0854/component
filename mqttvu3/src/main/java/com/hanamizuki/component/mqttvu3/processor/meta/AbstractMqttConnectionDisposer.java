package com.hanamizuki.component.mqttvu3.processor.meta;

import com.hanamizuki.component.mqttvu3.entity.Box;
import com.hanamizuki.component.mqttvu3.entity.Ssl;
import com.hanamizuki.component.mqttvu3.entity.Sub;
import com.hanamizuki.component.mqttvu3.processor.LoadingTLS;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public abstract class AbstractMqttConnectionDisposer implements LoadingTLS {

    public MqttPahoClientFactory mqttClientFactory(Box box) throws Exception {
        return this.mqttClientFactory(box.getUsername(),
                box.getPassword(),
                box.getConnectionTimeOut(),
                box.getCleanSession(),
                box.getKeepAlive(),
                box.getAutomaticReconnect(),
                box.getSsl(),
                box.getHost());
    }

    public MqttPahoClientFactory mqttClientFactory(Sub sub) throws Exception {
        return this.mqttClientFactory(sub.getUsername(),
                sub.getPassword(),
                sub.getConnectionTimeOut(),
                sub.getCleanSession(),
                sub.getKeepAlive(),
                sub.getAutomaticReconnect(),
                sub.getSsl(),
                sub.getHost());
    }

    public MqttPahoClientFactory mqttClientFactory(String username, String password,
                                                   Integer connectionTimeOut,
                                                   Boolean isCleanSession,
                                                   Integer keepAlive,
                                                   Boolean automaticReconnect,
                                                   Ssl ssl,
                                                   String... host) throws Exception{
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(host);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(connectionTimeOut);
        options.setCleanSession(isCleanSession);
        options.setKeepAliveInterval(keepAlive);
        options.setAutomaticReconnect(automaticReconnect);

        SSLContext sslContext = null;

        if (ssl != null) {
            sslContext = this.load(ssl);
        }

        if (sslContext != null){
            options.setSocketFactory(sslContext.getSocketFactory());
        }

        factory.setConnectionOptions(options);
        return factory;
    }


    @Override
    public SSLContext load(String TlsVersion,
                              Ssl.Locate locate,
                              Ssl.Keystore keyStoreType,
                              String keyStorePath,
                              String keyStorePassword,
                              Ssl.Keystore trustStoreType,
                              String trustStorePath,
                              String trustStorePassword) throws Exception{
        //  单向认证
        KeyManagerFactory kmf = null;
        if (keyStoreType != null){
            kmf = keyFactory(locate, keyStoreType, keyStorePath, keyStorePassword);
        }

        //  双向认证
        TrustManagerFactory tmf = null;
        if (trustStoreType != null){
            tmf = trustFactory(locate, trustStoreType, trustStorePath, trustStorePassword);
        }

        if (tmf == null){
            throw new RuntimeException("TrustStore type is invalid");
        }

        SSLContext sslContext = SSLContext.getInstance(TlsVersion);
        sslContext.init(kmf == null ? null : kmf.getKeyManagers(),tmf.getTrustManagers(),new SecureRandom());

        return sslContext;
    }

    public SSLContext load(Ssl ssl) throws Exception {
        if (ssl == null){
            throw  new RuntimeException("SSL object is invalid");
        }
        return this.load(ssl.getTlsVersion(),ssl.getLocate(),
                ssl.getKeyStoreType(),ssl.getKeyStorePath(),ssl.getKeyStorePassword(),
                ssl.getTrustStoreType(),ssl.getTrustStorePath(),ssl.getTrustStorePassword());
    }

    private KeyManagerFactory keyFactory(Ssl.Locate locate, Ssl.Keystore keystore, String path, String password) throws Exception {

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        KeyStore keyStore = KeyStore.getInstance(keystore.name().toUpperCase());
        if (locate == Ssl.Locate.PACKAGE){
            try(InputStream keyStoreStream = getClass().getClassLoader()
                    .getResourceAsStream(path)){
                keyStore.load(keyStoreStream, password.toCharArray());
            }
        }else{
            try (FileInputStream ksStream = new FileInputStream(path)) {
                keyStore.load(ksStream, password.toCharArray());
            }
        }

        kmf.init(keyStore, password.toCharArray());
        return kmf;
    }

    private TrustManagerFactory trustFactory(Ssl.Locate locate, Ssl.Keystore keystore, String path, String password) throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance(keystore.name().toUpperCase());
        if (locate == Ssl.Locate.PACKAGE){
            try(InputStream keyStoreStream = getClass().getClassLoader()
                    .getResourceAsStream(path)){
                keyStore.load(keyStoreStream, password.toCharArray());
            }
        }else {
            try (FileInputStream ksStream = new FileInputStream(path)) {
                keyStore.load(ksStream, password.toCharArray());
            }
        }
        tmf.init(keyStore);
        return tmf;
    }

}
