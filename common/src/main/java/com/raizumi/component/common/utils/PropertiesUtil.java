package com.raizumi.component.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;
import java.util.Objects;

public class PropertiesUtil {
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Map<String, Object> getProperties(String path, String target, String regex) {
        YamlMapFactoryBean bean = new YamlMapFactoryBean();
        bean.setResources(new ClassPathResource(path));
        Map<String, Object> maps = bean.getObject();
        if (maps == null) {
            log.error("Property file {} isn't exist.", path);
            return null;
        }
        //  .符号需要转义
        if (Objects.equals(regex, ".")) {
            regex = "\\.";
        }
        String[] strings = target.split(regex);
        for (String part : strings) {
            Object object = maps.get(part);
            if (object == null) {
                maps = null;
                break;
            }
            maps = convertToMap(object);
        }
        return maps;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertToMap(Object obj) {
        if (obj == null) return null;

        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }

        if (obj instanceof String) {
            try {
                return mapper.readValue((String) obj, Map.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable convert message to map.", e);
            }
        }

        // 尝试把 JavaBean 转换成 Map
        return mapper.convertValue(obj, Map.class);
    }

}
