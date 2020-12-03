package win.panyong.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by pan on 2018/1/10.
 */

public class ObjectUtil {

    public static Map<String, Object> jsonStringToMap(String json) {
        return JSON.parseObject(json, Map.class);
    }

    public static String mapToJsonString(Map<String, Object> map) {
        return JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String objectToJsonString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> T jsonStringToObject(String jsonString, Class<T> t) {
        return JSON.parseObject(jsonString, t);
    }

    public static <T> T jsonStringToObject(String jsonString, TypeReference<T> typeReference) {
        return JSON.parseObject(jsonString, typeReference);
    }

    public static <T> T jsonObjectToObject(JSONObject jsonObject, Class<T> t) {
        return JSON.parseObject(jsonObject.toJSONString(), t);
    }

    public static Map<String, Object> objectToMap(Object obj) {
        return JSON.parseObject(JSON.toJSONString(obj), Map.class);
    }

    public static <T> List<T> jsonStringToArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    public static Map<String, String> xmlStringToMap(String xml) {
        Map<String, String> map = new HashMap<String, String>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (doc == null)
            return map;
        Element root = doc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            map.put(e.getName(), e.getText());
        }
        return map;
    }


    public static void mapToXmlString(Map map, StringBuffer sb) {
        for (Object aSet : map.keySet()) {
            String key = (String) aSet;
            Object value = map.get(key);
            if (null == value)
                value = "";
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<").append(key).append(">");
                for (Object aList : list) {
                    HashMap hm = (HashMap) aList;
                    mapToXmlString(hm, sb);
                }
                sb.append("</").append(key).append(">");
            } else {
                if (value instanceof HashMap) {
                    sb.append("<").append(key).append(">");
                    mapToXmlString((HashMap) value, sb);
                    sb.append("</").append(key).append(">");
                } else {
                    sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
                }
            }
        }
    }

    //序列化对象
    public static byte[] serialize(Object obj) {
        if (obj == null)
            return null;
        ObjectOutputStream oos = null;
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            res = baos.toByteArray();
        } catch (IOException e) {
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
            return res;
        }
    }

    //反序列化
    public static <T> T unSerialize(byte[] bytes, T t) {
        if (bytes == null)
            return null;
        ObjectInputStream ois = null;
        T res = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            res = (T) ois.readObject();
        } catch (IOException e) {
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
            return res;
        }
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

    /**
     * 复制对象的属性
     *
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyProperties(Object source, Object target) throws Exception {
        Field[] sourceField = source.getClass().getDeclaredFields();
        Field[] targetField = target.getClass().getDeclaredFields();
        for (Field f1 : sourceField) {
            for (Field f2 : targetField) {
                if (f1.getName().equals(f2.getName()) && f1.getType().equals(f2.getType())) {
                    f1.setAccessible(true);
                    f2.setAccessible(true);
                    f2.set(target, f1.get(source));
                }
            }
        }
    }


    /**
     * 将list或set集合转成字符串
     *
     * @param source
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> String collectToString(Collection<T> source) {
        return JSONArray.toJSONString(source);
    }

    /**
     * 将符合map格式的字符串转成JSONObject对象
     *
     * @param string
     * @return
     */
    @Deprecated
    public static JSONObject stringToObject(String string) {
        JsonObject returnData = new JsonParser().parse(string).getAsJsonObject();
        return JSONObject.parseObject(string);
    }


}