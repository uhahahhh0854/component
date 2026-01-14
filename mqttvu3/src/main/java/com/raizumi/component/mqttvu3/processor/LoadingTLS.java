package com.raizumi.component.mqttvu3.processor;

import com.raizumi.component.mqttvu3.entity.Ssl;

import javax.net.ssl.SSLContext;

public interface LoadingTLS {

    SSLContext load(Ssl ssl) throws Exception;

    SSLContext load(String TlsVersion,
                    Ssl.Locate locate,
                    Ssl.Keystore keyStoreType,
                    String keyStorePath,
                    String keyStorePassword,
                    Ssl.Keystore trustStoreType,
                    String trustStorePath,
                    String trustStorePassword) throws Exception;




}
