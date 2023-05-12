package org.sixLoadTester.utils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.exceptions.UnhandledHttpMethodException;

public class HttpUtils {
    public static HttpRequestBase createHttpRequest(Request requestData) throws UnhandledHttpMethodException {
        HttpRequestBase request;
        if (requestData.method == HttpMethod.POST) {
            var postRequest = new HttpPost(requestData.endpoint);
            postRequest.setEntity(new StringEntity(requestData.body, ContentType.APPLICATION_JSON));
            request = postRequest;
        } else if (requestData.method == HttpMethod.GET) {
            request = new HttpGet(requestData.endpoint);
        } else if (requestData.method == HttpMethod.PUT) {
            request = new HttpPut(requestData.endpoint);
        } else if (requestData.method == HttpMethod.DELETE) {
            request = new HttpPut(requestData.endpoint);
        }
        else {
            throw new UnhandledHttpMethodException();
        }
        request.setHeader("Content-Type", "application/json");
        return request;
    }
}
