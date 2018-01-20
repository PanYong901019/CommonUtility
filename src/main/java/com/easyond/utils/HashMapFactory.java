package com.easyond.utils;

import java.util.HashMap;

/**
 * HashMap工厂类
 * Created by Administrator on 2017-10-19.
 */
@Deprecated
public class HashMapFactory {

    private HashMapFactory() {
        throw new RuntimeException("创建工具类异常!");
    }

    /**
     * 根据给定的长度创建不扩容的HashMap
     *
     * @param dataLength
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> generateHashMap(final int dataLength) {
        if (dataLength <= 12) {
            return new HashMap<K, V>();
        }
        int basicNumber = 24;
        int basicLength = 32;
        while (basicNumber <= dataLength) {
            basicNumber <<= 1;
            basicLength <<= 1;
        }
        return new HashMap<K, V>(basicLength);
    }

}



































