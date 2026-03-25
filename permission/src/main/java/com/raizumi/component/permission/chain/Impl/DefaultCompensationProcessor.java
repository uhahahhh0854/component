package com.raizumi.component.permission.chain.Impl;

import com.raizumi.component.permission.chain.CompensationDescriptor;

import javax.servlet.http.HttpServletResponse;

public class DefaultCompensationProcessor implements CompensationDescriptor<HttpServletResponse> {

    @Override
    public void handle(HttpServletResponse response) {

    }

    @Override
    public int order() {
        return 0;
    }
}

