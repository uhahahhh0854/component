package com.hanamizuki.component.common.handler;

public interface DEHandleDescriptor {
    default byte[] encrypt(byte[] data){
        return data;
    }

    default byte[] decrypt(byte[] data){
        return data;
    }
}
