package org.sixLoadTester.utils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.exceptions.UnhandledHttpMethodException;

public class HttpUtils {
    public static HttpRequestBase createHttpRequest(RequestData requestData) throws UnhandledHttpMethodException {
        if (requestData.method == HttpMethod.POST) {
            var request = new HttpPost(requestData.endpoint);
            request.setHeader("Content-Type", "application/json");
            var requestEntity = new StringEntity(requestData.jsonData, ContentType.APPLICATION_JSON);
            request.setEntity(requestEntity);

            return request;
        } else if (requestData.method == HttpMethod.GET) {
            var request = new HttpGet(requestData.endpoint);
            request.setHeader("Content-Type", "application/json");

            return request;
        }

        throw new UnhandledHttpMethodException();
    }

}
