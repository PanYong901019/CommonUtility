package com.easyond.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pan on 2018/1/10.
 */
public class StringUtil {


    public static boolean invalid(String str) {
        return str == null || "".equals(str.trim());
    }


    public static boolean isHave(String[] strs, String s) {
        for (String str : strs) {
            if (str.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static List<String[]> doRegex(String content, String regex, Integer groupAmount) {
        List<String[]> result = new ArrayList<String[]>();
        String[] strings = new String[groupAmount];
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            for (int i = 0; i <= groupAmount; i++) {
                strings[i] = i == 0 ? matcher.group() : matcher.group(i);
            }
            result.add(strings);
        }
        return result;
    }

    public static String InputStreamToString(InputStream in) {
        InputStreamReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //生成指定长度code
    public static String genCode(int size) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String number = "";
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            number += str.charAt(r.nextInt(str.length()));
        }
        return number;
    }

    /**
     * 获取系统流水号
     *
     * @param length   指定流水号长度
     * @param isNumber 指定流水号是否全由数字组成
     */
    public static String getSysJournalNo(int length, boolean isNumber) {
        //replaceAll()之后返回的是一个由十六进制形式组成的且长度为32的字符串
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (uuid.length() > length) {
            uuid = uuid.substring(0, length);
        } else if (uuid.length() < length) {
            for (int i = 0; i < length - uuid.length(); i++) {
                uuid = uuid + Math.round(Math.random() * 9);
            }
        }
        if (isNumber) {
            return uuid.replaceAll("a", "1").replaceAll("b", "2").replaceAll("c", "3").replaceAll("d", "4").replaceAll("e", "5").replaceAll("f", "6");
        } else {
            return uuid;
        }
    }

    /**
     * 金额元转分
     * <p>
     * 该方法会将金额中小数点后面的数值,四舍五入后只保留两位....如12.345-->12.35
     * 注意:该方法可处理贰仟万以内的金额
     * 注意:如果你的金额达到了贰仟万以上,则非常不建议使用该方法,否则计算出来的结果会令人大吃一惊
     *
     * @param amount 金额的元进制字符串
     * @return String 金额的分进制字符串
     */
    public static String moneyYuanToFenByRound(String amount) {
        if (invalid(amount)) {
            return amount;
        }
        if (-1 == amount.indexOf(".")) {
            return Integer.parseInt(amount) * 100 + "";
        }
        int money_fen = Integer.parseInt(amount.substring(0, amount.indexOf("."))) * 100;
        String pointBehind = (amount.substring(amount.indexOf(".") + 1));
        if (pointBehind.length() == 1) {
            return money_fen + Integer.parseInt(pointBehind) * 10 + "";
        }
        int pointString_1 = Integer.parseInt(pointBehind.substring(0, 1));
        int pointString_2 = Integer.parseInt(pointBehind.substring(1, 2));
        if (pointBehind.length() > 2) {
            int pointString_3 = Integer.parseInt(pointBehind.substring(2, 3));
            if (pointString_3 >= 5) {
                if (pointString_2 == 9) {
                    if (pointString_1 == 9) {
                        money_fen = money_fen + 100;
                        pointString_1 = 0;
                        pointString_2 = 0;
                    } else {
                        pointString_1 = pointString_1 + 1;
                        pointString_2 = 0;
                    }
                } else {
                    pointString_2 = pointString_2 + 1;
                }
            }
        }
        if (pointString_1 == 0) {
            return money_fen + pointString_2 + "";
        } else {
            return money_fen + pointString_1 * 10 + pointString_2 + "";
        }
    }

    public static LinkedHashMap<String, String> sign(String signKey, String algorithm, LinkedHashMap<String, String> paramsMap, String apart, boolean toLowerCase) {
        LinkedHashMap<String, String> ret = new LinkedHashMap<String, String>();
        String signature = "";
        StringBuilder s = StringUtil.invalid(signKey) ? new StringBuilder() : new StringBuilder("signid=" + signKey);
        for (String key : paramsMap.keySet()) {
            s.append(apart).append(toLowerCase ? key.toLowerCase() : key).append("=").append(paramsMap.get(key));
            ret.put(key, paramsMap.get(key));
        }
        try {
            MessageDigest crypt = MessageDigest.getInstance(algorithm);
            crypt.reset();
            crypt.update(s.toString().getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : crypt.digest()) {
                formatter.format("%02x", b);
            }
            signature = formatter.toString();
            formatter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ret.put("signature", signature);
        return ret;
    }

    public static boolean veryifySign(String signKey, String algorithm, LinkedHashMap<String, String> paramsMap, String apart, boolean toLowerCase, String signature) {
        LinkedHashMap<String, String> sign = sign(signKey, algorithm, paramsMap, apart, toLowerCase);
        return signature.equals(sign.get("signature"));
    }
}
