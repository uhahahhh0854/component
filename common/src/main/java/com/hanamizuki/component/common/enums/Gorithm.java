package com.hanamizuki.component.common.enums;

public enum Gorithm {
    CBC("AES/CBC/PKCS5Padding",16),
    CTR("AES/CTR/NoPadding",16),
    CFB("AES/CFB/NoPadding",16),
    OFB("AES/OFB/NoPadding",16),
    GCM("AES/GCM/NoPadding",12);
    private final String algorithm;
    private final Integer iv;

    public static Gorithm getType(String type){
        for(Gorithm t : Gorithm.values()){
            if(t.name().equalsIgnoreCase(type)){
                return t;
            }
        }
        return null;
    }

    Gorithm(String algorithm, Integer iv) {
        this.algorithm = algorithm;
        this.iv = iv;
    }

    public Integer getIv() {
        return iv;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
