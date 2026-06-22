package com.hanamizuki.component.mqttvu3.entity;

public class Ssl {
    public String getTlsVersion() {
        return TlsVersion;
    }

    public void setTlsVersion(String tlsVersion) {
        TlsVersion = tlsVersion;
    }

    public Locate getLocate() {
        return locate;
    }

    public void setLocate(Locate locate) {
        this.locate = locate;
    }

    public Keystore getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(Keystore keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public Keystore getTrustStoreType() {
        return trustStoreType;
    }

    public void setTrustStoreType(Keystore trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    Ssl() {
        keyStoreType = null;
        trustStoreType = null;
    }

    Ssl(String trustStoreType, String trustStorePath, String trustStorePassword) {
        if (trustStoreType != null && trustStoreType.isEmpty() && trustStorePath != null && !trustStorePath.isEmpty()) {
            this.trustStoreType = Keystore.toKeystore(trustStoreType);
            if (this.trustStoreType == null) {
                throw new RuntimeException("Trust store type is invalid");
            }
            this.trustStorePath = trustStorePath;
            this.trustStorePassword = trustStorePassword;
        }
    }

    Ssl(String keyStoreType, String keyStorePath, String keyStorePassword,
        String trustStoreType, String trustStorePath, String trustStorePassword) {
        if (keyStoreType != null && keyStoreType.isEmpty() && keyStorePath != null && keyStorePath.isEmpty()) {
            this.keyStoreType = Keystore.toKeystore(keyStoreType.toUpperCase());
            if (this.keyStoreType == null) {
                throw new RuntimeException("Keystore type is invalid");
            }
            this.keyStorePath = keyStorePath;
            this.keyStorePassword = keyStorePassword;
        }

        if (trustStoreType != null && trustStoreType.isEmpty() && trustStorePath != null && !trustStorePath.isEmpty()) {
            this.trustStoreType = Keystore.toKeystore(trustStoreType.toUpperCase());
            if (this.trustStoreType == null) {
                throw new RuntimeException("Trust store type is invalid");
            }
            this.trustStorePath = trustStorePath;
            this.trustStorePassword = trustStorePassword;
        }

    }


    public enum Locate {
        SYSTEM,
        PACKAGE,
    }

    public enum Keystore {
        JKS,
        PKCS12,
        DER,
        PEM;

        public static Keystore toKeystore(String type) {
            for (Keystore keystore : Keystore.values()) {
                if (keystore.name().equals(type)) {
                    return keystore;
                }
            }
            return null;
        }
    }

    private String TlsVersion = "TLSv1.2";

    private Locate locate = Locate.PACKAGE;

    //  客户端证书
    private Keystore keyStoreType = null;

    private String keyStorePath = null;

    private String keyStorePassword = null;

    //  CA根证书
    private Keystore trustStoreType = null;

    private String trustStorePath = null;

    private String trustStorePassword = null;

    @Override
    public String toString() {
        return "Ssl{" +
                "TlsVersion='" + TlsVersion + '\'' +
                ", locate=" + locate +
                ", keyStoreType=" + keyStoreType +
                ", keyStorePath='" + keyStorePath + '\'' +
                ", keyStorePassword='" + keyStorePassword + '\'' +
                ", trustStoreType=" + trustStoreType +
                ", trustStorePath='" + trustStorePath + '\'' +
                ", trustStorePassword='" + trustStorePassword + '\'' +
                '}';
    }
}
