package org.example;

import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public abstract class EndpointTester {
    protected final HttpMethod method;
    protected final String endpoint;
    protected String jsonData;

    public EndpointTester(String endpoint, HttpMethod method) {
        this.method = method;
        this.endpoint = endpoint;
        this.jsonData = "";
    }

    public EndpointTester(String endpoint, HttpMethod method, String jsonData) {
        this(endpoint, method);
        this.jsonData = jsonData;
    }

    public abstract void execute() throws InterruptedException;

    public abstract void produceStatistics() throws NoDataAvailableException;

    protected void runThread(List<Long> responseTimes) {
        var httpClient = HttpClientBuilder.create().build();
        var startTime = System.currentTimeMillis();
        try {
            var request = HttpUtils.createHttpRequest(endpoint, method, jsonData);
            var response = httpClient.execute(request);
            var entity = response.getEntity();
            if (entity != null) {
                // Discard the response content
                entity.getContent().close();
            }
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        } catch (UnhandledHttpMethodException e) {
            System.err.println("Unhandled HTTP method: " + e.getMessage());
        }
        var endTime = System.currentTimeMillis();
        responseTimes.add(endTime - startTime);
    }
}
