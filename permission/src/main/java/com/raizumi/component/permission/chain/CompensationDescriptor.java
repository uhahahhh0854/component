package com.raizumi.component.permission.chain;

import javax.servlet.http.HttpServletResponse;

public interface CompensationDescriptor<T> {
    void handle(T t);

    int order();
}
