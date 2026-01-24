package com.raizumi.component.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class HttpUtil {


    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                headerMap.put(name, value);
            }
        }
        return headerMap;
    }

    public static List<String> getHeaders(HttpServletRequest request, String headerName) {
        Enumeration<String> headers = request.getHeaders(headerName);
        if (headers == null || !headers.hasMoreElements()) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        while (headers.hasMoreElements()) {
            result.add(headers.nextElement());
        }
        return result;
    }
}
