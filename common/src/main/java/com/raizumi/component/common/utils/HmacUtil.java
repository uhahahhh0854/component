package com.raizumi.component.common.utils;

import com.raizumi.component.common.enums.Headers;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

import static com.raizumi.component.common.global.StaVariable.LS;

public class HmacUtil {

    public enum HmacAlgorithm {
        HMAC_SHA256("HmacSHA256"),

        HMAC_SHA1("HmacSHA1"),

        HMAC_SHA512("HmacSHA512");

        private final String algorithm;

        HmacAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }

    /**
     * generate secretKey from masterKey by appKey
     */
    public static String generate(HmacAlgorithm hmacAlgorithm, String masterKey, String appKey) throws Exception {
        Mac mac = Mac.getInstance(hmacAlgorithm.getAlgorithm());
        mac.init(new SecretKeySpec(masterKey.getBytes(StandardCharsets.UTF_8), hmacAlgorithm.getAlgorithm()));
        byte[] hmac = mac.doFinal(appKey.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmac);
    }

    /**
     * generate signature
     */
    public static String sign(HmacAlgorithm hmacAlgorithm, String secretKey, String data) throws Exception {
        Mac mac = Mac.getInstance(hmacAlgorithm.algorithm);
        mac.init(new SecretKeySpec(
                Base64.getDecoder().decode(secretKey),
                hmacAlgorithm.algorithm
        ));
        byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(raw);
    }

    /**
     * verify signature
     */
    public static boolean verify(HmacAlgorithm hmacAlgorithm,
                                 String masterKey,
                                 String appKey,
                                 Long timestamp,
                                 Long limitedTimestamp,
                                 String data,
                                 String signature) throws Exception {
        if (timestamp == null || limitedTimestamp == null) return false;
        long now = System.currentTimeMillis();
        if (Math.abs(now - timestamp) > limitedTimestamp) {
            return false;
        }

        String secretKey = generate(hmacAlgorithm, masterKey, appKey);

        String expectedSignature = sign(hmacAlgorithm, secretKey, data + timestamp);

        return MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                signature.getBytes(StandardCharsets.UTF_8)
        );
    }

    public static String rData(HttpServletRequest request) {
        StringBuilder result = new StringBuilder();

        String method = request.getMethod();

        if (method != null){
            result.append(method);
        }else{
            throw new IllegalArgumentException("Method not supported");
        }

        result.append(LS);

        String requestURI = request.getRequestURI();

        if (requestURI != null){
            result.append(requestURI);
        }else{
            throw new IllegalArgumentException("RequestURI not supported");
        }

        result.append(LS);

        String queryString = request.getQueryString();

        if (queryString != null){
            result.append(queryString);
        }

        result.append(LS);

        String bodyData = null;

        if (request instanceof ContentCachingRequestWrapper){
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;

            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                bodyData = new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            }
        }

        if (bodyData != null) {
            result.append(bodyData);
        }

        result.append(LS);


        List<String> nonces = HttpUtil.getHeaders(request, Headers.X_NONCE.name());

        if (nonces.size() == 1) {
            result.append(nonces.get(0));
        }else{
            throw new IllegalArgumentException("Request does not contain exactly one nonce");
        }

        result.append(LS);

        List<String> timestamps = HttpUtil.getHeaders(request, Headers.X_TIMESTAMP.name());
        if (timestamps.size() == 1) {
            result.append(new StringBuilder(timestamps.get(0)).reverse());
        }else{
            throw new IllegalArgumentException("Request does not contain exactly one timestamp");
        }

        result.append(LS);


        return result.toString();
    }


}
