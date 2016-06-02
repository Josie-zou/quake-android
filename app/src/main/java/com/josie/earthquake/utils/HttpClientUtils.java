package com.josie.earthquake.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntityHC4;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Josie on 16/5/12.
 */
public class HttpClientUtils {

    private static CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties().build();

    public static String doPost(String url, Map<String, String> params) throws IOException {
        HttpPostHC4 httpPost = new HttpPostHC4(url);
        List<NameValuePair> nvps = new ArrayList<>();
        for (String key : params.keySet()) {
            String value = params.get(key);
            nvps.add(new BasicNameValuePair(key, value));
        }
        httpPost.setEntity(new UrlEncodedFormEntityHC4(nvps, "utf-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        response.close();
        return result;
    }

    public static String doGet(String url) throws  Exception {
        HttpGetHC4 httpGetHC = new HttpGetHC4(url);
        CloseableHttpResponse response = httpClient.execute(httpGetHC);
        String result = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        response.close();
        return result;
    }

}
