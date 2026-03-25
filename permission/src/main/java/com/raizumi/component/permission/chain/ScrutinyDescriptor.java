package com.raizumi.component.permission.chain;

public interface ScrutinyDescriptor {

    boolean support();

    boolean verify();

    int order();
}
