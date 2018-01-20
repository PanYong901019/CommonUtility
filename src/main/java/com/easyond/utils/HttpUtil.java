package com.easyond.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pan on 2018/1/10.
 */
public class HttpUtil {

    public static String doHttpGet(String url, Map<String, String> parameterMap) throws Exception {
        String result;
        String protocol = url.split(":")[0];
        HttpClient client;
        if (!StringUtil.invalid(protocol) && protocol.equals("https")) {
            client = new SSLClient();
        } else {
            client = new DefaultHttpClient();
        }
        HttpResponse response;
        if (parameterMap != null) {
            StringBuilder urlBuilder = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.toString().substring(0, url.length() - 1);
        }
        HttpGet get = new HttpGet(url);
        response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        return result;
    }

    public static String doHttpGet(String url, Map<String, String> parameterMap, Map<String, String> headerMap) throws Exception {
        String result;
        String protocol = url.split(":")[0];
        HttpClient client;
        if (!StringUtil.invalid(protocol) && protocol.equals("https")) {
            client = new SSLClient();
        } else {
            client = new DefaultHttpClient();
        }
        HttpResponse response;
        if (parameterMap != null) {
            StringBuilder urlBuilder = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.toString().substring(0, url.length() - 1);
        }
        HttpGet get = new HttpGet(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
        }
        response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        return result;
    }

    public static String doHttpPost(String url, Map<String, String> parameterMap) throws Exception {
        String result;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        ArrayList<NameValuePair> parameters = null;
        if (parameterMap != null) {
            parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(parameters, "utf8"));
        response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        return result;
    }

    public static String doHttpPost(String url, Map<String, String> parameterMap, Map<String, String> headerMap) throws Exception {
        String result;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        ArrayList<NameValuePair> parameters = null;
        if (parameterMap != null) {
            parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        return result;
    }

    public static String doHttpPostXML(String url, String xml) throws Exception {
        String result;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(xml, ContentType.APPLICATION_XML));
        response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        return result;
    }

    public static String doHttpPostJson(String url, String json) throws Exception {
        String result;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        return result;
    }

    public static String getTinyUrl(String url) throws Exception {
        String tinyurl = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", url);
        String result = doHttpPost("http://dwz.cn/create.php", map);
        Map<String, Object> objectMap = ObjectUtil.jsonStringToMap(result);
        tinyurl = (String) objectMap.get("tinyurl");
        return tinyurl;
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


    private static class SSLClient extends DefaultHttpClient {
        SSLClient() throws Exception {
            super();
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
    }

}
