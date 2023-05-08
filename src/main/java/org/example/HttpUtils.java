package org.example;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class HttpUtils {
    public static HttpRequestBase createHttpRequest(String endpoint, HttpMethod method, String jsonData) throws UnhandledHttpMethodException {
        if (method == HttpMethod.POST) {
            var request = new HttpPost(endpoint);
            request.setHeader("Content-Type", "application/json");
            var requestEntity = new StringEntity(jsonData, ContentType.APPLICATION_JSON);
            request.setEntity(requestEntity);

            return request;
        } else if (method == HttpMethod.GET) {
            var request = new HttpGet(endpoint);
            request.setHeader("Content-Type", "application/json");

            return request;
        }

        throw new UnhandledHttpMethodException();
    }

}
