package com.easyond.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Pan on 2018/1/10.
 */
public class ConfigFileUtil {
    public static Map<String, String> resourcePropertyFileToMap(String resourcePropertyFileName) {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = ConfigFileUtil.class.getClassLoader().getResourceAsStream(resourcePropertyFileName);
            properties.load(is);
        } catch (Exception e) {
            System.out.println("不能读取配置文件 ：" + resourcePropertyFileName);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return new LinkedHashMap<String, String>((Map) properties);
    }

    public static Map<String, String> propertyFileInputStreamToMap(InputStream propertyFileInputStream) {
        Properties properties = new Properties();
        try {
            properties.load(propertyFileInputStream);
        } catch (Exception e) {
            System.out.println("不能读取配置文件");
        }
        return new LinkedHashMap<String, String>((Map) properties);
    }

}
