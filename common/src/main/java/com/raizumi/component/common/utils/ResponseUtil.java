package com.raizumi.component.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseUtil {

    private final ObjectMapper objectMapper;

    public ResponseUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    private void prepare(HttpServletResponse response, String contentType, HttpStatus status) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(contentType);
        response.setStatus(status.value());
    }

    public void writeJson(HttpServletResponse response, Object data, HttpStatus status) {
        if (response.isCommitted()) return;

        prepare(response, MediaType.APPLICATION_JSON_VALUE, status);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(data));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON response", e);
        }
    }

    public void writeText(HttpServletResponse response, String content, MediaType mediaType, HttpStatus status) {
        if (response.isCommitted()) return;

        prepare(response, mediaType.toString(), status);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write text response", e);
        }
    }

    public void writeBinary(HttpServletResponse response, byte[] data, String fileName, MediaType mediaType) {
        if (response.isCommitted()) return;

        try{
            response.setContentType(mediaType.toString());
            if (fileName != null && !fileName.isEmpty()) {
                String encodedName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"");
            }

            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(data);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException("Failed to write binary response", e);
            }
        }catch (IOException e) {
            throw new RuntimeException("Failed to encode filename", e);
        }
    }
}