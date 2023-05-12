package org.sixLoadTester.utils;

import org.apache.http.client.methods.*;
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
        } else if (requestData.method == HttpMethod.PUT) {
            var putRequest = new HttpPut(requestData.endpoint);
            putRequest.setEntity(new StringEntity(requestData.body, ContentType.APPLICATION_JSON));
            request = putRequest;
        } else if (requestData.method == HttpMethod.GET) {
            request = new HttpGet(requestData.endpoint);
        } else if (requestData.method == HttpMethod.DELETE) {
            request = new HttpDelete(requestData.endpoint);
        }
        else {
            throw new UnhandledHttpMethodException();
        }
        request.setHeader("Content-Type", "application/json");
        return request;
    }
}
