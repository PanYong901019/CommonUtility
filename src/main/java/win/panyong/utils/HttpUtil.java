package win.panyong.utils;


import com.alibaba.fastjson2.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pan on 2018/1/10.
 */
public class HttpUtil {

    public static String doHttpGet(String url, JSONObject parameter, JSONObject header) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        if (parameter != null) {
            StringBuilder urlBuilder = new StringBuilder(url + (url.contains("?") ? "&" : "?"));
            parameter.keySet().forEach(key -> urlBuilder.append(key).append("=").append(parameter.getString(key)).append("&"));
            url = urlBuilder.substring(0, urlBuilder.toString().length() - 1);
        }
        HttpGet get = new HttpGet(url);
        if (header != null) {
            header.keySet().forEach(key -> get.setHeader(key, header.getString(key)));
        }
        CloseableHttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpPost(String url, JSONObject parameter, JSONObject header) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        List<NameValuePair> parameters;
        if (parameter != null) {
            parameters = new ArrayList<>();
            parameter.keySet().forEach(key -> parameters.add(new BasicNameValuePair(key, parameter.getString(key))));
        } else {
            parameters = null;
        }
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
        if (header != null) {
            header.keySet().forEach(key -> post.setHeader(key, header.getString(key)));
        }
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpPostXML(String url, String xml, JSONObject header) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        if (header != null) {
            header.keySet().forEach(key -> post.setHeader(key, header.getString(key)));
        }
        post.setEntity(new StringEntity(xml, ContentType.APPLICATION_XML));
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpPostJson(String url, String json, JSONObject header) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        if (header != null) {
            header.keySet().forEach(key -> post.setHeader(key, header.getString(key)));
        }
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String getBaiduTinyUrl(String url) throws Exception {
        String tinyurl = null;
        JSONObject map = new JSONObject();
        map.put("url", url);
        String result = doHttpPost("http://dwz.cn/create.php", map, null);
        Map<String, Object> objectMap = ObjectUtil.jsonStringToMap(result);
        tinyurl = (String) objectMap.get("tinyurl");
        return tinyurl;
    }

    public static String getSinaTinyUrl(String url) throws Exception {
        String tinyurl = null;
        JSONObject map = new JSONObject();
        map.put("source", "3271760578");
        map.put("url_long", url);
        String result = doHttpPost("http://api.t.sina.com.cn/short_url/shorten.json", map, null);
        Map<String, Object> objectMap = ObjectUtil.jsonStringToMap(result);
        tinyurl = (String) objectMap.get("url_short");
        return tinyurl;
    }

    public static File download(String url, String fileName, String suffix, JSONObject parameter, JSONObject header) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        if (parameter != null) {
            StringBuilder urlBuilder = new StringBuilder(url + (url.contains("?") ? "&" : "?"));
            parameter.keySet().forEach(key -> urlBuilder.append(key).append("=").append(parameter.getString(key)).append("&"));
            url = urlBuilder.substring(0, urlBuilder.toString().length() - 1);
        }
        HttpGet get = new HttpGet(url);
        if (header != null) {
            header.keySet().forEach(key -> get.setHeader(key, header.getString(key)));
        }
        CloseableHttpResponse response = client.execute(get);
        byte[] bytes = EntityUtils.toByteArray(response.getEntity());
        File tempFile = File.createTempFile(fileName, suffix);
        FileUtil.doWriterFile(bytes, tempFile);
        response.close();
        return tempFile;
    }

    private static String getContentCharset(HttpResponse response) {
        String charset = "utf-8";
        Header header = response.getEntity().getContentType();
        if (header != null) {
            String s = header.getValue();
            if (matcher(s, "(charset)\\s?=\\s?(utf-?8)")) {
                charset = "utf-8";
            } else if (matcher(s, "(charset)\\s?=\\s?(gbk)")) {
                charset = "gbk";
            } else if (matcher(s, "(charset)\\s?=\\s?(gb2312)")) {
                charset = "gb2312";
            }
        }
        return charset;
    }

    private static boolean matcher(String s, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher matcher = p.matcher(s);
        return matcher.find();
    }

}
