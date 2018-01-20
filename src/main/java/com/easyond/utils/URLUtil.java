package com.easyond.utils;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Deprecated
public class URLUtil {

    private URLUtil() {
        throw new RuntimeException("创建工具类异常!");
    }

    /**
     * 根据给定url请求返回正文
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String requestContent(String url) throws Exception {
        URL targetUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
        conn.connect();
        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String record = null;
        while ((record = reader.readLine()) != null) {
            builder.append(record);
        }
        return builder.toString();
    }

}



















































