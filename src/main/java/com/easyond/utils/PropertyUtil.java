package com.easyond.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
@Deprecated
public class PropertyUtil {

    private PropertyUtil() {
        throw new RuntimeException("创建工具类异常!");
    }

    /**
     * 根据给定key得到application.properties文件中对应的值
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) throws Exception {
        Properties p = new Properties();
        InputStream in = new FileInputStream(PropertyUtil.class.getResource("/application.properties").getFile());
        p.load(in);
        return p.getProperty(key);
    }
}











































